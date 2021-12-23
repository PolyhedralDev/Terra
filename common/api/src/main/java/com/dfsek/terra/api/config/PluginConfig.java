/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.config;

import com.dfsek.terra.api.Platform;


public interface PluginConfig {
    void load(Platform platform);
    
    boolean dumpDefaultConfig();
    
    boolean isDebugCommands();
    
    boolean isDebugProfiler();
    
    boolean isDebugScript();
    
    int getBiomeSearchResolution();
    
    int getStructureCache();
    
    int getSamplerCache();
    
    int getMaxRecursion();
    
    int getProviderCache();
}
