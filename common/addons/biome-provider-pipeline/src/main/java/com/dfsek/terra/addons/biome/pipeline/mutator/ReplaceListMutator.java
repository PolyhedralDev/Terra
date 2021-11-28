/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.mutator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.dfsek.terra.addons.biome.pipeline.api.BiomeMutator;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.TerraBiome;


public class ReplaceListMutator implements BiomeMutator {
    private final Map<TerraBiome, ProbabilityCollection<TerraBiome>> replace;
    private final NoiseSampler sampler;
    private final ProbabilityCollection<TerraBiome> replaceDefault;
    private final String defaultTag;
    
    public ReplaceListMutator(Map<TerraBiome, ProbabilityCollection<TerraBiome>> replace, String defaultTag,
                              ProbabilityCollection<TerraBiome> replaceDefault, NoiseSampler sampler) {
        this.replace = replace;
        this.sampler = sampler;
        this.defaultTag = defaultTag;
        this.replaceDefault = replaceDefault;
    }
    
    @Override
    public TerraBiome mutate(ViewPoint viewPoint, double x, double z, long seed) {
        TerraBiome center = viewPoint.getBiome(0, 0);
        if(replace.containsKey(center)) {
            TerraBiome biome = replace.get(center).get(sampler, x, z, seed);
            return biome == null ? viewPoint.getBiome(0, 0) : biome;
        }
        if(viewPoint.getBiome(0, 0).getTags().contains(defaultTag)) {
            TerraBiome biome = replaceDefault.get(sampler, x, z, seed);
            return biome == null ? viewPoint.getBiome(0, 0) : biome;
        }
        return center;
    }
    
    @Override
    public Iterable<TerraBiome> getBiomes(Iterable<TerraBiome> biomes) {
        Set<TerraBiome> biomeSet = new HashSet<>();
        
        Set<TerraBiome> reject = new HashSet<>();
        
        biomes.forEach(biome -> {
            if(!biome.getTags().contains(defaultTag) && !replace.containsKey(biome)) {
                biomeSet.add(biome);
            } else {
                reject.add(biome);
            }
        });
        biomeSet.addAll(replaceDefault.getContents().stream().flatMap(terraBiome -> {
            if(terraBiome == null) return reject.stream();
            return Stream.of(terraBiome);
        }).toList());
        replace.forEach((biome, collection) -> {
            biomeSet.addAll(collection.getContents().stream().map(terraBiome -> {
                if(terraBiome == null) return biome;
                return terraBiome;
            }).toList());
        });
        return biomeSet;
    }
}
