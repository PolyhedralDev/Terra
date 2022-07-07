package com.dfsek.terra.mod.util;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Holder for hacky biome source seed workaround
 */
public class SeedHack {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeedHack.class);
    
    private static final Object2LongMap<MultiNoiseSampler> seedMap = new Object2LongOpenHashMap<>();
    
    public static long getSeed(MultiNoiseSampler sampler) {
        if(!seedMap.containsKey(sampler)) {
            throw new IllegalArgumentException("Sampler is not registered: " + sampler);
        }
        return seedMap.getLong(sampler);
    }
    
    public static void register(MultiNoiseSampler sampler, long seed) {
        LOGGER.info("Registered seed {} to sampler {}", seed, sampler.hashCode());
        seedMap.put(sampler, seed);
    }
}
