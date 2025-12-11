package com.dfsek.terra.addons.biome.extrusion.extrusions;

import com.dfsek.seismic.type.sampler.Sampler;
import com.dfsek.terra.addons.biome.extrusion.api.Extrusion;
import com.dfsek.terra.addons.biome.extrusion.api.ReplaceableBiome;
import com.dfsek.terra.addons.biome.query.api.BiomeQueries;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.collection.TriStateIntCache;
import com.dfsek.terra.api.util.range.Range;
import com.dfsek.terra.api.world.biome.Biome;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Sets biomes at locations based on a sampler.
 */
public class ReplaceExtrusion implements Extrusion {
    private final Sampler sampler;
    private final Range range;
    private final ProbabilityCollection<ReplaceableBiome> biomes;
    private final Predicate<Biome> hasTag;

    // Replaced ThreadLocal<HashMap> with a specialized primitive cache.
    // Shared across all threads safely.
    private final TriStateIntCache cache;

    public ReplaceExtrusion(Sampler sampler, Range range, ProbabilityCollection<ReplaceableBiome> biomes, String tag) {
        this.sampler = sampler;
        this.range = range;
        this.biomes = biomes;
        this.hasTag = BiomeQueries.has(tag);

        this.cache = new TriStateIntCache(65536);
    }

    @Override
    public Biome extrude(Biome original, int x, int y, int z, long seed) {
        int id = original.getIntID();

        long state = cache.get(id);
        boolean passes;

        if (state == TriStateIntCache.STATE_UNSET) {
            // Only run the test if unset in cache
            passes = hasTag.test(original);
            cache.set(id, passes);
        } else {
            // Read the primitive long directly
            passes = (state == TriStateIntCache.STATE_TRUE);
        }

        if (passes) {
            if (range.isInRange(y)) {
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
