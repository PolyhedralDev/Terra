package com.dfsek.terra.api.addon;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.registry.CheckedRegistry;


public interface AddonLoader {
    /**
     * Load all addons.
     * @param platform TerraPlugin instance.
     */
    void load(Platform platform, CheckedRegistry<Addon> addons);
}
