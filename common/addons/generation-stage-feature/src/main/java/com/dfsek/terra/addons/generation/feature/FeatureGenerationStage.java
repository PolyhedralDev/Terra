/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.generation.feature;

import com.dfsek.terra.addons.generation.feature.config.BiomeFeatures;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.PopulationUtil;
import com.dfsek.terra.api.util.StringIdentifiable;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.chunk.generation.ProtoWorld;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;

import java.util.Random;


public class FeatureGenerationStage implements GenerationStage, StringIdentifiable {
    private final Platform platform;
    
    private final String id;
    
    public FeatureGenerationStage(Platform platform, String id) {
        this.platform = platform;
        this.id = id;
    }
    
    @Override
    @SuppressWarnings("try")
    public void populate(ProtoWorld world) {
        try(ProfileFrame ignore = platform.getProfiler().profile("feature_stage:" + id)) {
            int cx = world.centerChunkX() << 4;
            int cz = world.centerChunkZ() << 4;
            long seed = world.getSeed();
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    int tx = cx + x;
                    int tz = cz + z;
                    ColumnImpl<ProtoWorld> column = new ColumnImpl<>(tx, tz, world);
                    world.getBiomeProvider().getBiome(tx, tz, seed).getContext().get(BiomeFeatures.class).getFeatures().forEach(feature -> {
                        try(ProfileFrame ignored = platform.getProfiler().profile(feature.getID())) {
                            if(feature.getDistributor().matches(tx, tz, seed)) {
                                feature.getLocator()
                                       .getSuitableCoordinates(column)
                                       .forEach(y ->
                                                        feature.getStructure(world, tx, y, tz)
                                                               .generate(new Vector3(tx, y, tz), world, new Random(
                                                                                 PopulationUtil.getCarverChunkSeed(world.centerChunkX(),
                                                                                                                   world.centerChunkZ(), seed)),
                                                                         Rotation.NONE)
                                               );
                            }
                        }
                    });
                }
            }
        }
    }
    
    @Override
    public String getID() {
        return id;
    }
}
