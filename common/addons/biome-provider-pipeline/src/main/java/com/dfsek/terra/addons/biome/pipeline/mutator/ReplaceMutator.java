/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.mutator;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeMutator;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.TerraBiome;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;


public class ReplaceMutator implements BiomeMutator {
    private final String replaceableTag;
    private final ProbabilityCollection<TerraBiome> replace;
    private final NoiseSampler sampler;
    
    public ReplaceMutator(String replaceable, ProbabilityCollection<TerraBiome> replace, NoiseSampler sampler) {
        this.replaceableTag = replaceable;
        this.replace = replace;
        this.sampler = sampler;
    }
    
    @Override
    public TerraBiome mutate(ViewPoint viewPoint, double x, double z, long seed) {
        if(viewPoint.getBiome(0, 0).getTags().contains(replaceableTag)) {
            TerraBiome biome = replace.get(sampler, x, z, seed);
            return biome == null ? viewPoint.getBiome(0, 0) : biome;
        }
        return viewPoint.getBiome(0, 0);
    }
    
    @Override
    public Iterable<TerraBiome> getBiomes(Iterable<TerraBiome> biomes) {
        Set<TerraBiome> biomeSet = new HashSet<>();
        Set<TerraBiome> reject = new HashSet<>();
        biomes.forEach(biome -> {
            if(!biome.getTags().contains(replaceableTag)) {
                biomeSet.add(biome);
            } else {
                reject.add(biome);
            }
        });
        biomeSet.addAll(replace.getContents().stream().flatMap(terraBiome -> {
            if(terraBiome == null) return reject.stream();
            return Stream.of(terraBiome);
        }).toList());
        return biomeSet;
    }
}
