/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.config;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.registry.key.RegistryKey;


public interface PluginConfig {
    void load(Platform platform);

    boolean dumpDefaultConfig();

    boolean isDebugCommands();

    boolean isDebugProfiler();

    boolean isDebugScript();

    boolean isDebugLog();

    String getBiomeKeyFormat();

    default RegistryKey getBiomeKey(ConfigPack pack, RegistryKey biomeKey) {
        String format = this.getBiomeKeyFormat()
            .replace("%pack_id%", pack.getID().toLowerCase())
            .replace("%biome_namespace%", biomeKey.getNamespace().toLowerCase())
            .replace("%biome_id%", biomeKey.getID().toLowerCase());
        if (format.contains(":")) {
            return RegistryKey.parse(format);
        } else {
            return RegistryKey.of("terra", format);
        }
    }

    int getBiomeSearchResolution();

    int getStructureCache();

    int getSamplerCache();

    int getMaxRecursion();

    int getProviderCache();
}
