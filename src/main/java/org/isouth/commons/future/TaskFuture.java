package org.isouth.commons.future;

import java.util.concurrent.Future;

public interface TaskFuture<V> extends Future<V> {

    /**
     * check if the task complete successfully.
     * 
     * @return {@code true} if the task complete successfully.
     */
    boolean isSuccess();

    Throwable cause();

    TaskFuture<V> await() throws InterruptedException;

    TaskFuture<V> awaitUninterruptibly();

    boolean await(long timeoutMillis) throws InterruptedException;

    boolean awaitUninterruptibly(long timeoutMillis);

    V getNow();

    void onComplete(TaskCallback<? extends TaskFuture<? super V>> callback);

    void onSuccess(TaskCallback<? extends TaskFuture<? super V>> callback);

    void onFailure(TaskCallback<? extends TaskFuture<? super V>> callback);
}
