package com.dfsek.terra.api.addon;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.registry.CheckedRegistry;


public interface AddonLoader {
    /**
     * Load all addons.
     * @param main TerraPlugin instance.
     */
    void load(TerraPlugin main, CheckedRegistry<Addon> addons);
}
