package org.isouth.commons.future;

public abstract class TaskCallback<F extends TaskFuture<?>> extends Functional<F, F> {

    @Override
    public abstract F apply(F f);
}
