/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.config;

import java.util.List;

import com.dfsek.terra.api.Platform;


public interface PluginConfig {
    void load(Platform platform);

    boolean isDebugCommands();

    boolean isDebugProfiler();

    boolean isDebugScript();

    boolean isDebugLog();

    int getBiomeSearchResolution();

    int getStructureCache();

    int getSamplerCache();

    int getMaxRecursion();

    List<String> getIgnoredResources();

    int getProviderCache();
}
