package com.dfsek.terra.mod.util;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.world.level.biome.Climate.Sampler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Holder for hacky biome source seed workaround
 */
public class SeedHack {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeedHack.class);

    private static final Object2LongMap<Sampler> seedMap = new Object2LongOpenHashMap<>();

    public static long getSeed(Sampler sampler) {
        if(!seedMap.containsKey(sampler)) {
            throw new IllegalArgumentException("Sampler is not registered: " + sampler);
        }
        return seedMap.getLong(sampler);
    }

    public static void register(Sampler sampler, long seed) {
        LOGGER.info("Registered seed {} to sampler {}", seed, sampler.hashCode());
        seedMap.put(sampler, seed);
    }
}
