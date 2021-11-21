/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.tectonic;

import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

import java.lang.reflect.Type;
import java.util.function.Supplier;


public interface LoaderHolder {
    <T> LoaderHolder applyLoader(Type type, TypeLoader<T> loader);
    
    default <T> LoaderHolder applyLoader(Class<? extends T> type, TypeLoader<T> loader) {
        return applyLoader((Type) type, loader);
    }
    
    <T> LoaderHolder applyLoader(Type type, Supplier<ObjectTemplate<T>> loader);
    
    default <T> LoaderHolder applyLoader(Class<? extends T> type, Supplier<ObjectTemplate<T>> loader) {
        return applyLoader((Type) type, loader);
    }
}
