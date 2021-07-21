package com.dfsek.terra.addons.feature;

import com.dfsek.tectonic.annotations.Final;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.config.AbstractableTemplate;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.structure.Structure;
import com.dfsek.terra.api.structure.feature.Distributor;
import com.dfsek.terra.api.structure.feature.Locator;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;

public class FeatureTemplate implements AbstractableTemplate {
    @Value("id")
    @Final
    private String id;

    @Value("distributor")
    private @Meta Distributor distributor;

    @Value("locator")
    private @Meta Locator locator;

    @Value("structures.distribution")
    private @Meta NoiseSampler structureNoise;

    @Value("structures.structures")
    private @Meta ProbabilityCollection<Structure> structures;

    @Override
    public String getID() {
        return id;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public Locator getLocator() {
        return locator;
    }

    public NoiseSampler getStructureNoise() {
        return structureNoise;
    }

    public ProbabilityCollection<Structure> getStructures() {
        return structures;
    }
}
