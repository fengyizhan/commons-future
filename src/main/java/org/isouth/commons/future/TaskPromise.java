package org.isouth.commons.future;

public interface TaskPromise<V> extends TaskFuture<V> {

    TaskFuture<V> getFuture();

    void setSuccess(V value);

    void setFailure(Throwable cause);
}
