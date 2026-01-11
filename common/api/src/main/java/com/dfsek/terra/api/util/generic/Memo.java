/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.util.generic;

import com.dfsek.terra.api.util.generic.control.Monad;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;


public final class Memo<T> implements Monad<T, Memo<?>> {
    private final Supplier<T> valueSupplier;
    private volatile T value = null;
    private final AtomicBoolean got = new AtomicBoolean(false);

    private Memo(Supplier<T> valueSupplier) {
        this.valueSupplier = valueSupplier;
    }

    public static <T> Memo<T> lazy(Supplier<T> valueSupplier) {
        return new Memo<>(valueSupplier);
    }

    public static <T> Memo<T> of(T value) {
        return new Memo<>(() -> value);
    }

    public T value() {
        if(!got.compareAndExchange(false, true)) {
            value = valueSupplier.get();
        }
        return value;
    }

    @Override
    public @NotNull <T2> Memo<T2> bind(@NotNull Function<T, Monad<T2, Memo<?>>> map) {
        return lazy(() -> ((Memo<T2>) map.apply(value())).value());
    }

    @Override
    public @NotNull <U> Memo<U> map(@NotNull Function<T, U> map) {
        return lazy(() -> map.apply(value()));
    }

    @Override
    public @NotNull <T1> Memo<T1> pure(@NotNull T1 t) {
        return new Memo<>(() -> t);
    }
}
