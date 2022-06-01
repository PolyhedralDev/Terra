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

import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.stage.type.BiomeMutator;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public class ReplaceListMutator implements BiomeMutator {
    private final Map<BiomeDelegate, ProbabilityCollection<BiomeDelegate>> replace;
    private final NoiseSampler sampler;
    private final ProbabilityCollection<BiomeDelegate> replaceDefault;
    private final String defaultTag;
    
    public ReplaceListMutator(Map<BiomeDelegate, ProbabilityCollection<BiomeDelegate>> replace, String defaultTag,
                              ProbabilityCollection<BiomeDelegate> replaceDefault, NoiseSampler sampler) {
        this.replace = replace;
        this.sampler = sampler;
        this.defaultTag = defaultTag;
        this.replaceDefault = replaceDefault;
    }
    
    @Override
    public BiomeDelegate mutate(ViewPoint viewPoint, double x, double z, long seed) {
        BiomeDelegate center = viewPoint.getBiome(0, 0);
        if(replace.containsKey(center)) {
            BiomeDelegate biome = replace.get(center).get(sampler, x, z, seed);
            return biome.isSelf() ? viewPoint.getBiome(0, 0) : biome;
        }
        if(viewPoint.getBiome(0, 0).getTags().contains(defaultTag)) {
            BiomeDelegate biome = replaceDefault.get(sampler, x, z, seed);
            return biome.isSelf() ? viewPoint.getBiome(0, 0) : biome;
        }
        return center;
    }
    
    @Override
    public Iterable<BiomeDelegate> getBiomes(Iterable<BiomeDelegate> biomes) {
        Set<BiomeDelegate> biomeSet = new HashSet<>();
        
        Set<BiomeDelegate> reject = new HashSet<>();
        
        biomes.forEach(biome -> {
            if(!biome.getTags().contains(defaultTag) && !replace.containsKey(biome)) {
                biomeSet.add(biome);
            } else {
                reject.add(biome);
            }
        });
        biomeSet.addAll(replaceDefault.getContents().stream().flatMap(terraBiome -> {
            if(terraBiome.isSelf()) return reject.stream();
            return Stream.of(terraBiome);
        }).toList());
        replace.forEach((biome, collection) -> biomeSet.addAll(collection.getContents().stream().map(terraBiome -> {
            if(terraBiome.isSelf()) return biome;
            return terraBiome;
        }).toList()));
        return biomeSet;
    }
}
