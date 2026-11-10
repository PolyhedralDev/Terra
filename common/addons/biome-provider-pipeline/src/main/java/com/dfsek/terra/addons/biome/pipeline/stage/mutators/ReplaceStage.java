/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.stage.mutators;

import com.dfsek.seismic.type.sampler.Sampler;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.api.biome.PipelineBiome;
import com.dfsek.terra.addons.biome.pipeline.pipeline.BiomeChunkImpl;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.tag.BiomeTag;


public class ReplaceStage implements Stage {
    private final BiomeTag replaceableTag;
    private final ProbabilityCollection<PipelineBiome> replace;
    private final Sampler sampler;

    public ReplaceStage(BiomeTag replaceable, ProbabilityCollection<PipelineBiome> replace, Sampler sampler) {
        this.replaceableTag = replaceable;
        this.replace = replace;
        this.sampler = sampler;
    }

    @Override
    public PipelineBiome apply(BiomeChunkImpl.ViewPoint viewPoint) {
        if(replaceableTag.has(viewPoint.getBiome().getIntID())) {
            PipelineBiome biome = replace.get(sampler, viewPoint.worldX(), viewPoint.worldZ(), viewPoint.worldSeed());
            return biome.isSelf() ? viewPoint.getBiome() : biome;
        }
        return viewPoint.getBiome();
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
            if(!biome.getTags().contains(replaceableTag.name())) {
                biomeSet.add(biome);
            } else {
                reject.add(biome);
            }
        });
        biomeSet.addAll(replace.getContents().stream().flatMap(terraBiome -> {
            if(terraBiome.isSelf()) return reject.stream();
            return Stream.of(terraBiome);
        }).toList());
        return biomeSet;
    }
}
