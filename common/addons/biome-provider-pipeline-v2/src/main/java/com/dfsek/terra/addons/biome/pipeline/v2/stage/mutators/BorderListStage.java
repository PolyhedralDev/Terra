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
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import com.dfsek.terra.addons.biome.pipeline.v2.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.v2.api.biome.PipelineBiome;
import com.dfsek.terra.addons.biome.pipeline.v2.pipeline.BiomeChunkImpl;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.vector.Vector2Int;


public class BorderListStage implements Stage {
    private final String border;
    private final NoiseSampler noiseSampler;
    private final ProbabilityCollection<PipelineBiome> replaceDefault;
    private final String defaultReplace;
    private final Map<PipelineBiome, ProbabilityCollection<PipelineBiome>> replace;
    
    private final Vector2Int[] borderPoints;
    
    public BorderListStage(Map<PipelineBiome, ProbabilityCollection<PipelineBiome>> replace, String border, String defaultReplace,
                           NoiseSampler noiseSampler, ProbabilityCollection<PipelineBiome> replaceDefault) {
        this.border = border;
        this.noiseSampler = noiseSampler;
        this.replaceDefault = replaceDefault;
        this.defaultReplace = defaultReplace;
        this.replace = replace;
    
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
    public Iterable<PipelineBiome> getBiomes(Iterable<PipelineBiome> biomes) {
        Set<PipelineBiome> biomeSet = new HashSet<>();
        biomes.forEach(biomeSet::add);
        biomeSet.addAll(replaceDefault.getContents().stream().filter(Predicate.not(PipelineBiome::isSelf)).toList());
        replace.forEach((biome, collection) -> biomeSet.addAll(collection.getContents()));
        return biomeSet;
    }
    
    @Override
    public PipelineBiome apply(BiomeChunkImpl.ViewPoint viewPoint) {
        PipelineBiome center = viewPoint.getBiome();
        if(center.getTags().contains(defaultReplace)) {
            for(Vector2Int point : borderPoints) {
                PipelineBiome current = viewPoint.getRelativeBiome(point.getX(), point.getZ());
                if(current != null && current.getTags().contains(border)) {
                    if(replace.containsKey(center)) {
                        PipelineBiome replacement = replace.get(center).get(noiseSampler, viewPoint.worldX(), viewPoint.worldZ(),
                                                                      viewPoint.worldSeed());
                        return replacement.isSelf() ? center : replacement;
                    }
                    PipelineBiome replacement = replaceDefault.get(noiseSampler, viewPoint.worldX(), viewPoint.worldZ(), viewPoint.worldSeed());
                    return replacement.isSelf() ? center : replacement;
                }
            }
        }
        return center;
    }
    
    @Override
    public int maxRelativeReadDistance() {
        return 1;
    }
}
