package com.dfsek.terra.addons.biome.extrusion.config.extrusions;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.biome.extrusion.api.Extrusion;
import com.dfsek.terra.addons.biome.extrusion.api.ReplaceableBiome;
import com.dfsek.terra.addons.biome.extrusion.extrusions.ReplaceExtrusion;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.tag.BiomeTag;


public class ReplaceExtrusionTemplate extends SamplerExtrusionTemplate {
    @Value("to")
    private @Meta ProbabilityCollection<@Meta ReplaceableBiome> biomes;

    @Value("from")
    private @Meta BiomeTag from;

    @Override
    public Extrusion get() {
        return new ReplaceExtrusion(sampler, range, biomes, from);
    }
}
