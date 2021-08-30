package com.dfsek.terra.config.pack;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;

import java.util.HashSet;
import java.util.Set;

import com.dfsek.terra.api.addon.TerraAddon;


public class ConfigPackAddonsTemplate implements ConfigTemplate {
    @Value("addons")
    @Default
    private final Set<TerraAddon> addons = new HashSet<>();
    
    
    public Set<TerraAddon> getAddons() {
        return addons;
    }
}
