/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.config;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.util.reflection.TypeKey;


public interface ConfigType<T extends AbstractableTemplate, R> {
    T getTemplate(ConfigPack pack, Platform platform);
    
    ConfigFactory<T, R> getFactory();
    
    TypeKey<R> getTypeKey();
}
