package org.isouth.commons.future;

import java.util.function.Function;

public abstract class Functional<T, R> {

    public abstract R apply(T t);

    public <V> Functional<V, R> compose(final Function<? super V, ? extends T> before) {
        return new Functional<V, R>() {
            @Override
            public R apply(V v) {
                return Functional.this.apply(before.apply(v));
            }
        };
    }

    public <V> Functional<T, V> andThen(final Function<? super R, ? extends V> after) {
        return new Functional<T, V>() {
            @Override
            public V apply(T t) {
                return after.apply(Functional.this.apply(t));
            }
        };
    }
}
