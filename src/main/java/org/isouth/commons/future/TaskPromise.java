package org.isouth.commons.future;

public interface TaskPromise extends TaskFuture {

    TaskFuture getFuture();

    TaskPromise setSuccess(Object result);

    boolean trySuccess(Object result);

    TaskPromise setFailure(Throwable cause);

    boolean tryFailure(Throwable cause);
}
