/*
 * Copyright (c) 2020-2023 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.v2.stage.mutators;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.dfsek.terra.addons.biome.pipeline.v2.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.v2.api.biome.PipelineBiome;
import com.dfsek.terra.addons.biome.pipeline.v2.pipeline.BiomeChunkImpl;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public class ReplaceListStage implements Stage {
    private final Map<PipelineBiome, ProbabilityCollection<PipelineBiome>> replace;
    private final NoiseSampler sampler;
    private final ProbabilityCollection<PipelineBiome> replaceDefault;
    private final String defaultTag;
    
    public ReplaceListStage(Map<PipelineBiome, ProbabilityCollection<PipelineBiome>> replace, String defaultTag,
                            ProbabilityCollection<PipelineBiome> replaceDefault, NoiseSampler sampler) {
        this.replace = replace;
        this.sampler = sampler;
        this.defaultTag = defaultTag;
        this.replaceDefault = replaceDefault;
    }
    
    @Override
    public PipelineBiome apply(BiomeChunkImpl.ViewPoint viewPoint) {
        PipelineBiome center = viewPoint.getBiome();
        if(replace.containsKey(center)) {
            PipelineBiome biome = replace.get(center).get(sampler, viewPoint.worldX(), viewPoint.worldZ(), viewPoint.worldSeed());
            return biome.isSelf() ? viewPoint.getBiome() : biome;
        }
        if(viewPoint.getBiome().getTags().contains(defaultTag)) {
            PipelineBiome biome = replaceDefault.get(sampler, viewPoint.worldX(), viewPoint.worldZ(), viewPoint.worldSeed());
            return biome.isSelf() ? viewPoint.getBiome() : biome;
        }
        return center;
    }
    
    @Override
    public int maxRelativeReadDistance() {
        return 0;
    }
    
    @Override
    public Iterable<PipelineBiome> getBiomes(Iterable<PipelineBiome> biomes) {
        Set<PipelineBiome> biomeSet = new HashSet<>();
        
        Set<PipelineBiome> reject = new HashSet<>();
        
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
