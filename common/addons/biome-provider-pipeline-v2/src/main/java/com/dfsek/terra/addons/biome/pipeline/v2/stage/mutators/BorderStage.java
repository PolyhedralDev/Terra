/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.v2.stage.mutators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.dfsek.terra.addons.biome.pipeline.v2.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.v2.api.biome.PipelineBiome;
import com.dfsek.terra.addons.biome.pipeline.v2.pipeline.BiomeChunkImpl;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.vector.Vector2Int;


public class BorderStage implements Stage {
    private final String border;
    private final NoiseSampler noiseSampler;
    private final ProbabilityCollection<PipelineBiome> replace;
    private final String replaceTag;
    private final Vector2Int[] borderPoints;
    
    public BorderStage(String border, String replaceTag, NoiseSampler noiseSampler, ProbabilityCollection<PipelineBiome> replace) {
        this.border = border;
        this.noiseSampler = noiseSampler;
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
                    PipelineBiome replacement = replace.get(noiseSampler, viewPoint.worldX(), viewPoint.worldZ(), viewPoint.worldSeed());
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
