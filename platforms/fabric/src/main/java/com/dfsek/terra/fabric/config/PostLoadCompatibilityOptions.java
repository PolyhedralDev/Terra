package com.dfsek.terra.fabric.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.util.seeded.SeededTerraBiome;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("FieldMayBeFinal")
public class PostLoadCompatibilityOptions implements ConfigTemplate {
    @Value("structures.inject-biome.exclude-biomes")
    @Default
    private Map<SeededTerraBiome, Set<Identifier>> excludedPerBiomeStructures = new HashMap<>();

    @Value("features.inject-biome.exclude-biomes")
    @Default
    private Map<SeededTerraBiome, Set<Identifier>> excludedPerBiomeFeatures = new HashMap<>();

    public Map<SeededTerraBiome, Set<Identifier>> getExcludedPerBiomeFeatures() {
        return excludedPerBiomeFeatures;
    }

    public Map<SeededTerraBiome, Set<Identifier>> getExcludedPerBiomeStructures() {
        return excludedPerBiomeStructures;
    }
}
