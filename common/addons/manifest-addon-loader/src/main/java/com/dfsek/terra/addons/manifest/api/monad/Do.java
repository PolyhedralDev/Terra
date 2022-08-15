package com.dfsek.terra.addons.manifest.api.monad;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Function3;
import io.vavr.Function4;

import com.dfsek.terra.api.util.function.monad.Monad;


public final class Do {
    private Do() {
    
    }
    
    public static <T, M extends Monad<?, M>> Monad<T, M> with(Monad<T, M> monad) {
        return monad;
    }
    
    public static <T, U, M extends Monad<?, M>> Monad<U, M> with(Monad<T, M> monad,
                                                                 Function1<T, Monad<U, M>> bind) {
        return monad
                .bind(bind);
    }
    
    public static <T, U, V, M extends Monad<?, M>> Monad<V, M> with(Monad<T, M> monad,
                                                                    Function1<T, Monad<U, M>> bind,
                                                                    Function2<T, U, Monad<V, M>> bind2) {
        return monad
                .bind(t -> monad
                        .bind(bind)
                        .bind(bind2.apply(t)));
    }
    
    public static <T, U, V, M extends Monad<?, M>> Monad<V, M> with(Monad<T, M> monad,
                                                                    Monad<U, M> monad2,
                                                                    Function2<T, U, Monad<V, M>> bind2) {
        return with(monad, Function1.constant(monad2), bind2);
    }
    
    public static <T, U, V, W, M extends Monad<?, M>> Monad<W, M> with(Monad<T, M> monad,
                                                                       Function1<T, Monad<U, M>> bind,
                                                                       Function2<T, U, Monad<V, M>> bind2,
                                                                       Function3<T, U, V, Monad<W, M>> bind3) {
        return monad
                .bind(t -> monad
                        .bind(bind)
                        .bind(u -> bind2.apply(t)
                                        .andThen(vmMonad -> vmMonad
                                                .bind(bind3.apply(t, u)))
                                        .apply(u)));
    }
    
    public static <T, U, V, W, M extends Monad<?, M>> Monad<W, M> with(Monad<T, M> monad,
                                                                       Monad<U, M> monad2,
                                                                       Function2<T, U, Monad<V, M>> bind2,
                                                                       Function3<T, U, V, Monad<W, M>> bind3) {
        return with(monad, Function1.constant(monad2), bind2, bind3);
    }
    
    public static <T, U, V, W, M extends Monad<?, M>> Monad<W, M> with(Monad<T, M> monad,
                                                                       Monad<U, M> monad2,
                                                                       Monad<V, M> monad3,
                                                                       Function3<T, U, V, Monad<W, M>> bind3) {
        return with(monad, monad2, Function2.constant(monad3), bind3);
    }
    
    public static <T, U, V, W, X, M extends Monad<?, M>> Monad<X, M> with(Monad<T, M> monad,
                                                                          Function1<T, Monad<U, M>> bind,
                                                                          Function2<T, U, Monad<V, M>> bind2,
                                                                          Function3<T, U, V, Monad<W, M>> bind3,
                                                                          Function4<T, U, V, W, Monad<X, M>> bind4) {
        return monad
                .bind(t -> monad
                        .bind(bind)
                        .bind(u -> bind2.apply(t)
                                        .andThen(vmMonad -> vmMonad
                                                .bind(v -> bind3.apply(t, u)
                                                                .andThen(wmMonad -> wmMonad
                                                                        .bind(bind4.apply(t, u, v)))
                                                                .apply(v)))
                                        .apply(u)));
    }
    
    public static <T, U, V, W, X, M extends Monad<?, M>> Monad<X, M> with(Monad<T, M> monad,
                                                                          Monad<U, M> monad2,
                                                                          Function2<T, U, Monad<V, M>> bind2,
                                                                          Function3<T, U, V, Monad<W, M>> bind3,
                                                                          Function4<T, U, V, W, Monad<X, M>> bind4) {
        return with(monad, Function1.constant(monad2), bind2, bind3, bind4);
    }
    
    public static <T, U, V, W, X, M extends Monad<?, M>> Monad<X, M> with(Monad<T, M> monad,
                                                                          Monad<U, M> monad2,
                                                                          Monad<V, M> monad3,
                                                                          Function3<T, U, V, Monad<W, M>> bind3,
                                                                          Function4<T, U, V, W, Monad<X, M>> bind4) {
        return with(monad, monad2, Function2.constant(monad3), bind3, bind4);
    }
    
    public static <T, U, V, W, X, M extends Monad<?, M>> Monad<X, M> with(Monad<T, M> monad,
                                                                          Monad<U, M> monad2,
                                                                          Monad<V, M> monad3,
                                                                          Monad<W, M> monad4,
                                                                          Function4<T, U, V, W, Monad<X, M>> bind4) {
        return with(monad, monad2, monad3, Function3.constant(monad4), bind4);
    }
}
