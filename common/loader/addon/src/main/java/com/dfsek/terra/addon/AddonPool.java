package com.dfsek.terra.addon;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.dfsek.terra.addon.exception.AddonLoadException;


public class AddonPool {
    private final Map<String, PreLoadAddon> pool = new HashMap<>();
    
    public void add(PreLoadAddon addon) throws AddonLoadException {
        if(pool.containsKey(addon.getId())) {
            String message = String.format("Duplicate addon " +
                                           "ID: %s; original ID from file: %s, class: %sDuplicate ID from file: %s, class: %s",
                                           addon.getId(),
                                           pool.get(addon.getId()).getFile().getAbsolutePath(),
                                           pool.get(addon.getId()).getAddonClass().getCanonicalName(),
                                           addon.getFile().getAbsolutePath(),
                                           addon.getAddonClass().getCanonicalName());
            throw new AddonLoadException(message);
        }
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
