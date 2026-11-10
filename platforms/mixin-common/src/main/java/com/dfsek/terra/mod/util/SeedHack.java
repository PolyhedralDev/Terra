package com.dfsek.terra.mod.util;

import com.google.common.collect.MapMaker;
import net.minecraft.world.level.biome.Climate.Sampler;

import java.util.concurrent.ConcurrentMap;


/**
 * Maps climate samplers to world seeds.
 * <p>
 * {@link net.minecraft.world.level.levelgen.RandomState} creates a new {@link Sampler} for every sampling context.
 */
public class SeedHack {
    private static final ConcurrentMap<Sampler, Long> seedMap = new MapMaker().weakKeys().makeMap();

    public static long getSeed(Sampler sampler) {
        Long seed = seedMap.get(sampler);
        if(seed == null) {
            throw new IllegalArgumentException("Sampler is not registered: " + sampler);
        }
        return seed;
    }

    public static void register(Sampler sampler, long seed) {
        seedMap.put(sampler, seed);
    }
}
