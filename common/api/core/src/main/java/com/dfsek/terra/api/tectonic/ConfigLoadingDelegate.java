/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.tectonic;

import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;
import com.dfsek.tectonic.api.loader.type.TypeLoader;

import java.lang.reflect.Type;
import java.util.function.Supplier;


public interface ConfigLoadingDelegate {
    <T> ConfigLoadingDelegate applyLoader(Type type, TypeLoader<T> loader);
    
    default <T> ConfigLoadingDelegate applyLoader(Class<? extends T> type, TypeLoader<T> loader) {
        return applyLoader((Type) type, loader);
    }
    
    <T> ConfigLoadingDelegate applyLoader(Type type, Supplier<ObjectTemplate<T>> loader);
    
    default <T> ConfigLoadingDelegate applyLoader(Class<? extends T> type, Supplier<ObjectTemplate<T>> loader) {
        return applyLoader((Type) type, loader);
    }
}
