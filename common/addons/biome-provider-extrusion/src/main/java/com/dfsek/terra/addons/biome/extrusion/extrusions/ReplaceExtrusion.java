package com.dfsek.terra.addons.biome.extrusion.extrusions;

import com.dfsek.seismic.type.sampler.Sampler;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.dfsek.terra.addons.biome.extrusion.api.Extrusion;
import com.dfsek.terra.addons.biome.extrusion.api.ReplaceableBiome;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.range.Range;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.tag.BiomeTag;


/**
 * Sets biomes at locations based on a sampler.
 */
public class ReplaceExtrusion implements Extrusion {
    private final Sampler sampler;
    private final Range range;
    private final ProbabilityCollection<ReplaceableBiome> biomes;
    // Concrete final type keeps has() monomorphic for inlining.
    private final BiomeTag from;

    public ReplaceExtrusion(Sampler sampler, Range range, ProbabilityCollection<ReplaceableBiome> biomes, BiomeTag from) {
        this.sampler = sampler;
        this.range = range;
        this.biomes = biomes;
        this.from = from;
    }

    @Override
    public Biome extrude(Biome original, int x, int y, int z, long seed) {
        if(from.has(original.getIntID())) {
            if(range.isInRange(y)) {
                return biomes.get(sampler, x, y, z, seed).get(original);
            }
        }

        return original;
    }

    @Override
    public Collection<Biome> getBiomes() {
        return biomes
            .getContents()
            .stream()
            .filter(Predicate.not(ReplaceableBiome::isSelf))
            .map(ReplaceableBiome::get)
            .collect(Collectors.toSet());
    }
}
