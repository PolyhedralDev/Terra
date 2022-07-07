package com.dfsek.terra.addons.biome.extrusion.config.extrusions;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.biome.extrusion.api.Extrusion;
import com.dfsek.terra.addons.biome.extrusion.api.ReplaceableBiome;
import com.dfsek.terra.addons.biome.extrusion.extrusions.SetExtrusion;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public class SetExtrusionTemplate extends SamplerExtrusionTemplate {
    @Value("to")
    private @Meta ProbabilityCollection<@Meta ReplaceableBiome> biomes;
    
    @Override
    public Extrusion get() {
        return new SetExtrusion(sampler, range, biomes);
    }
}
