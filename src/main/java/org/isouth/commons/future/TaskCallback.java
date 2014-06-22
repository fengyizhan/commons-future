package org.isouth.commons.future;

public abstract class TaskCallback extends Functional<TaskFuture, TaskFuture> {

    /**
     * 执行动作
     */
    @Override
    public abstract TaskFuture apply(TaskFuture f);
}
