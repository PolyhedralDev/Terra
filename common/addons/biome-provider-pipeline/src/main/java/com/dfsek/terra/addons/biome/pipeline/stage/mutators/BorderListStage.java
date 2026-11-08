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
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import com.dfsek.terra.addons.biome.pipeline.api.Stage;
import com.dfsek.terra.addons.biome.pipeline.api.biome.PipelineBiome;
import com.dfsek.terra.addons.biome.pipeline.pipeline.BiomeChunkImpl;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.biome.tag.BiomeTag;


public class BorderListStage implements Stage {
    private final BiomeTag border;
    private final Sampler Sampler;
    private final ProbabilityCollection<PipelineBiome> replaceDefault;
    private final BiomeTag defaultReplace;
    private final Map<PipelineBiome, ProbabilityCollection<PipelineBiome>> replace;
    // By PipelineBiome#getIntID(); relies on one ID per biome name.
    private final ProbabilityCollection<PipelineBiome>[] replaceById;

    private final Vector2Int[] borderPoints;

    public BorderListStage(Map<PipelineBiome, ProbabilityCollection<PipelineBiome>> replace, BiomeTag border,
                           BiomeTag defaultReplace,
                           Sampler Sampler, ProbabilityCollection<PipelineBiome> replaceDefault) {
        this.border = border;
        this.Sampler = Sampler;
        this.replaceDefault = replaceDefault;
        this.defaultReplace = defaultReplace;
        this.replace = replace;
        this.replaceById = ReplaceTable.byId(replace);

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
        int centerId = center.getIntID();
        if(defaultReplace.has(centerId)) {
            for(Vector2Int point : borderPoints) {
                PipelineBiome current = viewPoint.getRelativeBiome(point.getX(), point.getZ());
                if(current != null && border.has(current.getIntID())) {
                    ProbabilityCollection<PipelineBiome> collection = centerId < replaceById.length ? replaceById[centerId] : null;
                    if(collection != null) {
                        PipelineBiome replacement = collection.get(Sampler, viewPoint.worldX(), viewPoint.worldZ(),
                            viewPoint.worldSeed());
                        return replacement.isSelf() ? center : replacement;
                    }
                    PipelineBiome replacement = replaceDefault.get(Sampler, viewPoint.worldX(), viewPoint.worldZ(),
                        viewPoint.worldSeed());
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
