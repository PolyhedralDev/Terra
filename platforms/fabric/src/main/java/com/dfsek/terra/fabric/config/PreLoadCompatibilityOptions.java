package com.dfsek.terra.fabric.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("FieldMayBeFinal")
public class PreLoadCompatibilityOptions implements ConfigTemplate {
    @Value("features.inject-registry.enable")
    @Default
    private boolean doRegistryInjection = false;

    @Value("features.inject-namespaces")
    @Default
    private Set<String> featureNamespaces = new HashSet<>();

    @Value("structures.inject-namespaces")
    @Default
    private Set<String> structureNamespaces = new HashSet<>();

    @Value("features.inject-registry.excluded-features")
    @Default
    private Set<Identifier> excludedRegistryFeatures = new HashSet<>();


    public Set<String> getFeatureNamespaces() {
        return featureNamespaces;
    }

    public Set<String> getStructureNamespaces() {
        return structureNamespaces;
    }

    public boolean doRegistryInjection() {
        return doRegistryInjection;
    }

    public Set<Identifier> getExcludedRegistryFeatures() {
        return excludedRegistryFeatures;
    }
}
