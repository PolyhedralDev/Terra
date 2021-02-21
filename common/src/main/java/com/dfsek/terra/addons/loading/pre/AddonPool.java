package com.dfsek.terra.addons.loading.pre;

import com.dfsek.terra.addons.loading.AddonLoadException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AddonPool {
    private final Map<String, PreLoadAddon> pool = new HashMap<>();

    public void add(PreLoadAddon addon) throws AddonLoadException {
        if(pool.containsKey(addon.getId()))
            throw new AddonLoadException("Duplicate addon ID: " + addon.getId());
        pool.put(addon.getId(), addon);
    }

    public PreLoadAddon get(String id) {
        return pool.get(id);
    }

    public void buildAll() throws AddonLoadException {
        for(PreLoadAddon value : pool.values()) {
            value.rebuildDependencies(this, value, true);
        }
    }

    public Set<PreLoadAddon> getAddons() {
        return new HashSet<>(pool.values());
    }
}
