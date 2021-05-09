package com.dfsek.terra.fabric.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.config.pack.ConfigPack;

import java.util.HashMap;
import java.util.Map;

public class FabricWorldConfig implements ConfigTemplate {
    @SuppressWarnings("FieldMayBeFinal")
    @Value(".")
    @Default
    private Map<String, ConfigPack> worlds;

    public FabricWorldConfig(Map<String, ConfigPack> worlds) {
        this.worlds = worlds;
    }

    public FabricWorldConfig() {
        this(new HashMap<>());
    }

    public Map<String, ConfigPack> getWorlds() {
        return worlds;
    }
}
