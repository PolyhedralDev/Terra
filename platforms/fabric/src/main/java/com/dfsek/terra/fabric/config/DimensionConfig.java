package com.dfsek.terra.fabric.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.config.pack.ConfigPack;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("FieldMayBeFinal")
public class DimensionConfig implements ConfigTemplate {
    @Value("dimensions")
    @Default
    private Map<Identifier, ConfigPack> dimensionPacks = new HashMap<>();

    public Map<Identifier, ConfigPack> getDimensionPacks() {
        return dimensionPacks;
    }
}
