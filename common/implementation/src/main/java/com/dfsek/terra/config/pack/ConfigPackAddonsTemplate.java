package com.dfsek.terra.config.pack;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.addon.TerraAddon;

import java.util.HashSet;
import java.util.Set;

public class ConfigPackAddonsTemplate implements ConfigTemplate {
    @Value("addons")
    @Default
    private Set<TerraAddon> addons = new HashSet<>();


    public Set<TerraAddon> getAddons() {
        return addons;
    }
}
