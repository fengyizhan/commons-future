package org.isouth.commons.future;

public abstract class TaskCallback extends Functional<TaskFuture, TaskFuture> {

    @Override
    public abstract TaskFuture apply(TaskFuture f);
}
