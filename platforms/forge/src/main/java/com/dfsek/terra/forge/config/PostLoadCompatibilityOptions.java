package com.dfsek.terra.forge.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;

import com.dfsek.terra.config.builder.BiomeBuilder;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@SuppressWarnings("FieldMayBeFinal")
public class PostLoadCompatibilityOptions implements ConfigTemplate {
    @Value("structures.inject-biome.exclude-biomes")
    @Default
    private Map<BiomeBuilder, Set<ResourceLocation>> excludedPerBiomeStructures = new HashMap<>();
    
    @Value("features.inject-biome.exclude-biomes")
    @Default
    private Map<BiomeBuilder, Set<ResourceLocation>> excludedPerBiomeFeatures = new HashMap<>();
    
    public Map<BiomeBuilder, Set<ResourceLocation>> getExcludedPerBiomeFeatures() {
        return excludedPerBiomeFeatures;
    }
    
    public Map<BiomeBuilder, Set<ResourceLocation>> getExcludedPerBiomeStructures() {
        return excludedPerBiomeStructures;
    }
}
