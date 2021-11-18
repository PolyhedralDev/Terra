package com.dfsek.terra.config.pack;

import ca.solostudios.strata.version.VersionRange;
import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.dfsek.terra.api.addon.BaseAddon;


@SuppressWarnings("FieldMayBeFinal")
public class ConfigPackAddonsTemplate implements ConfigTemplate {
    @Value("addons")
    @Default
    private Map<BaseAddon, VersionRange> addons = new HashMap<>();
    
    
    public Map<BaseAddon, VersionRange> getAddons() {
        return addons;
    }
}
