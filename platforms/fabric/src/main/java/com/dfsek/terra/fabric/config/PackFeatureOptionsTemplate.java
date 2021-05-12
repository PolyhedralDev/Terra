package com.dfsek.terra.fabric.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class PackFeatureOptionsTemplate implements ConfigTemplate {
    @Value("features.inject-registry.enable")
    @Default
    private boolean doRegistryInjection = false;

    @Value("features.inject-biome.enable")
    @Default
    private boolean doBiomeInjection = false;

    @Value("features.inject-biome.excluded-features")
    @Default
    private List<Identifier> excludedRegistryFeatures = new ArrayList<>();

    @Value("features.inject-biome.excluded-features")
    @Default
    private List<Identifier> excludedBiomeFeatures = new ArrayList<>();

    public boolean doBiomeInjection() {
        return doBiomeInjection;
    }

    public boolean doRegistryInjection() {
        return doRegistryInjection;
    }

    public List<Identifier> getExcludedBiomeFeatures() {
        return excludedBiomeFeatures;
    }

    public List<Identifier> getExcludedRegistryFeatures() {
        return excludedRegistryFeatures;
    }
}
