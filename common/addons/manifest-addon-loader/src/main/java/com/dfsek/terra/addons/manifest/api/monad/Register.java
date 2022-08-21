package com.dfsek.terra.addons.manifest.api.monad;

import com.dfsek.terra.api.registry.key.Keyed;
import com.dfsek.terra.api.util.function.monad.Monad;
import com.dfsek.terra.api.util.reflection.TypeKey;


public class Register {
    private static <T> Monad<Void, Init<?>> register(TypeKey<T> type, String key, T object) {
        return Init.unit(info -> {
            throw new UnsupportedOperationException("TODO");
        });
    }
    
    public static <T> Monad<Void, Init<?>> register(Class<T> clazz, String key, T object) {
        return register(TypeKey.of(clazz), key, object);
    }
    
    public static <T extends Keyed<T>> Monad<Void, Init<?>> register(Class<T> clazz, Keyed<T> keyed) {
        return register(TypeKey.of(clazz), keyed.getID(), keyed.coerce());
    }
}
