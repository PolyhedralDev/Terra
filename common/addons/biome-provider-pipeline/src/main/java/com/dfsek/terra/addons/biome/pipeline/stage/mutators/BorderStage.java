/*
 * Copyright (c) 2020-2025 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.stage.mutators;

import com.dfsek.seismic.type.sampler.Sampler;
import com.dfsek.seismic.type.vector.Vector2Int;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.api.biome.PipelineBiome;
import com.dfsek.terra.addons.biome.pipeline.pipeline.BiomeChunkImpl;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


public class BorderStage implements Stage {
    private final String border;
    private final Sampler Sampler;
    private final ProbabilityCollection<PipelineBiome> replace;
    private final String replaceTag;
    private final Vector2Int[] borderPoints;

    public BorderStage(String border, String replaceTag, Sampler Sampler, ProbabilityCollection<PipelineBiome> replace) {
        this.border = border;
        this.Sampler = Sampler;
        this.replace = replace;
        this.replaceTag = replaceTag;
        List<Vector2Int> points = new ArrayList<>();
        for(int x = -1; x <= 1; x++) {
            for(int z = -1; z <= 1; z++) {
                if(x == 0 && z == 0) continue;
                points.add(Vector2Int.of(x, z));
            }
        }
        this.borderPoints = points.toArray(new Vector2Int[0]);
    }

    @Override
    public PipelineBiome apply(BiomeChunkImpl.ViewPoint viewPoint) {
        PipelineBiome center = viewPoint.getBiome();
        if(center.getTags().contains(replaceTag)) {
            for(Vector2Int point : borderPoints) {
                PipelineBiome current = viewPoint.getRelativeBiome(point.getX(), point.getZ());
                if(current != null && current.getTags().contains(border)) {
                    PipelineBiome replacement = replace.get(Sampler, viewPoint.worldX(), viewPoint.worldZ(), viewPoint.worldSeed());
                    return replacement.isSelf() ? center : replacement;
                }
            }
        }
        return center;
    }

    @Override
    public Iterable<PipelineBiome> getBiomes(Iterable<PipelineBiome> biomes) {
        Set<PipelineBiome> biomeSet = new HashSet<>();
        biomes.forEach(biomeSet::add);
        biomeSet.addAll(
            replace
                .getContents()
                .stream()
                .filter(
                    Predicate.not(PipelineBiome::isSelf)
                )
                .toList()
        );
        return biomeSet;
    }

    @Override
    public int maxRelativeReadDistance() {
        return 1;
    }
}
