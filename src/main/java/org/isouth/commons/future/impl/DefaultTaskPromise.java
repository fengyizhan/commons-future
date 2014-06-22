package org.isouth.commons.future.impl;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.isouth.commons.future.TaskCallback;
import org.isouth.commons.future.TaskFuture;
import org.isouth.commons.future.TaskPromise;

public class DefaultTaskPromise implements TaskPromise {

    private static final Object VOID_SUCCESS = new Success(null);

    private volatile CountDownLatch complteLatch;

    private volatile ResultHolder result;

    private static interface ResultHolder {
        boolean isSuccess();

        boolean isFailure();

        Object getResult();
    }

    private static final class Success<R> implements ResultHolder {

        final R value;

        Success(R value) {
            this.value = value;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public boolean isFailure() {
            return false;
        }

        @Override
        public R getResult() {
            return value;
        }
    }

    private static final class Failure implements ResultHolder {

        Throwable t;

        Failure(Throwable t) {
            this.t = t;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @Override
        public Throwable getResult() {
            return t;
        }
    }

    @Override
    public boolean isSuccess() {
        ResultHolder result = this.result;
        return result != null && result.isSuccess();
    }

    @Override
    public Throwable cause() {
        ResultHolder result = this.result;
        if (result != null && result.isFailure()) {
            return ((Failure) result).getResult();
        }
        return null;
    }

    @Override
    public Object getNow() {
        ResultHolder result = this.result;
        if (result != null && result.isSuccess()) {
            return result.getResult();
        }
        return null;
    }

    @Override
    public TaskFuture await() throws InterruptedException {
        if (isDone()) {
            return this;
        }

        synchronized (this) {
            if (isDone()) {
                return this;
            }

            if (this.complteLatch == null) {
                this.complteLatch = new CountDownLatch(1);
            }
            this.complteLatch.await();
        }
        return this;
    }

    @Override
    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        if (isDone()) {
            return true;
        }

        synchronized (this) {
            if (isDone()) {
                return true;
            }
            if (this.complteLatch == null) {
                this.complteLatch = new CountDownLatch(1);
            }
            return this.complteLatch.await(timeout, unit);
        }
    }

    @Override
    public TaskFuture onComplete(final TaskCallback callback) {
        // TODO
        return this;
    }

    @Override
    public TaskFuture onSuccess(final TaskCallback callback) {
        return onComplete(new TaskCallback() {
            @Override
            public TaskFuture apply(final TaskFuture f) {
                if (f.isSuccess()) {
                    return callback.apply(f);
                }
                return f;
            }
        });
    }

    @Override
    public TaskFuture onFailure(final TaskCallback callback) {
        return onComplete(new TaskCallback() {
            @Override
            public TaskFuture apply(final TaskFuture f) {
                if (!f.isSuccess()) {
                    return callback.apply(f);
                }
                return f;
            }
        });
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return this.result != null;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        await();
        if (!isSuccess()) {
            throw new ExecutionException(((Failure) result).getResult());
        }
        return getNow();
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (!await(timeout, unit)) {
            throw new TimeoutException("Futures timed out after [" + unit.toMillis(timeout) + "ms]");
        }
        if (isDone() && !isSuccess()) {
            throw new ExecutionException(((Failure) result).getResult());
        }
        return getNow();
    }

    @Override
    public TaskFuture getFuture() {
        return this;
    }

    @Override
    public TaskPromise setSuccess(Object value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean trySuccess(Object value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public TaskPromise setFailure(Throwable cause) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean tryFailure(Throwable cause) {
        // TODO Auto-generated method stub
        return false;
    }

}
