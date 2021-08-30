package com.dfsek.terra.fabric.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.dfsek.terra.api.world.biome.TerraBiome;


@SuppressWarnings("FieldMayBeFinal")
public class PostLoadCompatibilityOptions implements ConfigTemplate {
    @Value("structures.inject-biome.exclude-biomes")
    @Default
    private Map<TerraBiome, Set<Identifier>> excludedPerBiomeStructures = new HashMap<>();
    
    @Value("features.inject-biome.exclude-biomes")
    @Default
    private Map<TerraBiome, Set<Identifier>> excludedPerBiomeFeatures = new HashMap<>();
    
    public Map<TerraBiome, Set<Identifier>> getExcludedPerBiomeFeatures() {
        return excludedPerBiomeFeatures;
    }
    
    public Map<TerraBiome, Set<Identifier>> getExcludedPerBiomeStructures() {
        return excludedPerBiomeStructures;
    }
}
