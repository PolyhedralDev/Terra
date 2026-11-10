/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.stage.mutators;

import com.dfsek.seismic.type.sampler.Sampler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.api.biome.PipelineBiome;
import com.dfsek.terra.addons.biome.pipeline.pipeline.BiomeChunkImpl;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.tag.BiomeTag;


public class ReplaceListStage implements Stage {
    private final Map<PipelineBiome, ProbabilityCollection<PipelineBiome>> replace;
    // By PipelineBiome#getIntID(); relies on one ID per biome name.
    private final ProbabilityCollection<PipelineBiome>[] replaceById;
    private final Sampler sampler;
    private final ProbabilityCollection<PipelineBiome> replaceDefault;
    private final BiomeTag defaultTag;

    public ReplaceListStage(Map<PipelineBiome, ProbabilityCollection<PipelineBiome>> replace, BiomeTag defaultTag,
                            ProbabilityCollection<PipelineBiome> replaceDefault, Sampler sampler) {
        this.replace = replace;
        this.replaceById = ReplaceTable.byId(replace);
        this.sampler = sampler;
        this.defaultTag = defaultTag;
        this.replaceDefault = replaceDefault;
    }

    @Override
    public PipelineBiome apply(BiomeChunkImpl.ViewPoint viewPoint) {
        PipelineBiome center = viewPoint.getBiome();
        int id = center.getIntID();
        ProbabilityCollection<PipelineBiome> collection = id < replaceById.length ? replaceById[id] : null;
        if(collection != null) {
            PipelineBiome biome = collection.get(sampler, viewPoint.worldX(), viewPoint.worldZ(), viewPoint.worldSeed());
            return biome.isSelf() ? viewPoint.getBiome() : biome;
        }
        if(defaultTag.has(id)) {
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
            if(!biome.getTags().contains(defaultTag.name()) && !replace.containsKey(biome)) {
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
