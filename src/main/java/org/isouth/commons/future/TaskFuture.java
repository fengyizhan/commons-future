package org.isouth.commons.future;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public interface TaskFuture extends Future<Object> {

    boolean isSuccess();

    Throwable cause();

    Object getNow();

    TaskFuture await() throws InterruptedException;

    boolean await(long timeout, TimeUnit unit) throws InterruptedException;

    TaskFuture onComplete(TaskCallback callback);

    TaskFuture onSuccess(TaskCallback callback);

    TaskFuture onFailure(TaskCallback callback);
}
