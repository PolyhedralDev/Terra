package com.dfsek.terra.addons.manifest.api.monad;

import com.dfsek.terra.addons.manifest.impl.InitInfo;
import com.dfsek.terra.api.util.function.Functions;
import com.dfsek.terra.api.util.function.monad.Monad;

import io.vavr.Function0;
import io.vavr.Function1;

import java.util.function.Consumer;
import java.util.function.Function;


public class Init<T> implements Monad<T, Init<?>> {
    private final Function<InitInfo, T> get;
    
    protected static <T> Init<T> of(Function<InitInfo, T> get) {
        return new Init<>(get);
    }
    protected static Init<Void> unit(Consumer<InitInfo> get) {
        return new Init<>(info -> {
            get.accept(info);
            return null;
        });
    }
    
    private Init(Function<InitInfo, T> get) {
        this.get = get;
    }
    
    public T apply(InitInfo platform) {
        return get.apply(platform);
    }
    
    @Override
    public <U> Init<U> bind(Function<T, Monad<U, Init<?>>> map) {
        return new Init<>(info -> ((Init<U>) map.apply(apply(info))).apply(info));
    }
    
    @Override
    public <U> Init<U> map(Function<T, U> fn) {
        return (Init<U>) Monad.super.map(fn);
    }
    
    @Override
    public <U> Monad<U, Init<?>> pure(U u) {
        return ofPure(u);
    }
    
    public static Monad<Void, Init<?>> unit() {
        return Init.unit(a -> {});
    }
    
    public static <T> Init<T> ofPure(T t) {
        return of(Functions.constant(t));
    }
}
