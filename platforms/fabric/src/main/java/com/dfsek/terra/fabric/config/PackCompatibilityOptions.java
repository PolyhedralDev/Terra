package com.dfsek.terra.fabric.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.config.builder.BiomeBuilder;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("FieldMayBeFinal")
public class PackCompatibilityOptions implements ConfigTemplate {
    @Value("features.inject-registry.enable")
    @Default
    private boolean doRegistryInjection = false;

    @Value("features.inject-biome.enable")
    @Default
    private boolean doBiomeInjection = false;

    @Value("features.inject-registry.excluded-features")
    @Default
    private Set<Identifier> excludedRegistryFeatures = new HashSet<>();

    @Value("features.inject-biome.excluded-features")
    @Default
    private Set<Identifier> excludedBiomeFeatures = new HashSet<>();

    @Value("features.inject-biome.exclude-biomes")
    @Default
    private Map<BiomeBuilder, Set<Identifier>> excludedPerBiomeFeatures = new HashMap<>();

    @Value("structures.inject-biome.excluded-features")
    @Default
    private Set<Identifier> excludedBiomeStructures = new HashSet<>();

    @Value("structures.inject-biome.exclude-biomes")
    @Default
    private Map<BiomeBuilder, Set<Identifier>> excludedPerBiomeStructures = new HashMap<>();

    public boolean doBiomeInjection() {
        return doBiomeInjection;
    }

    public boolean doRegistryInjection() {
        return doRegistryInjection;
    }

    public Set<Identifier> getExcludedBiomeFeatures() {
        return excludedBiomeFeatures;
    }

    public Set<Identifier> getExcludedRegistryFeatures() {
        return excludedRegistryFeatures;
    }

    public Map<BiomeBuilder, Set<Identifier>> getExcludedPerBiomeFeatures() {
        return excludedPerBiomeFeatures;
    }

    public Map<BiomeBuilder, Set<Identifier>> getExcludedPerBiomeStructures() {
        return excludedPerBiomeStructures;
    }

    public Set<Identifier> getExcludedBiomeStructures() {
        return excludedBiomeStructures;
    }
}
