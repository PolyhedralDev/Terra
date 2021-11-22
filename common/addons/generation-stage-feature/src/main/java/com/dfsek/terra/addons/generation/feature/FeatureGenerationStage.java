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
import com.dfsek.terra.api.structure.rotation.Rotation;
import com.dfsek.terra.api.util.PopulationUtil;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.generator.GenerationStage;


public class FeatureGenerationStage implements GenerationStage {
    private final Platform platform;
    
    public FeatureGenerationStage(Platform platform) {
        this.platform = platform;
    }
    
    @Override
    @SuppressWarnings("try")
    public void populate(World world, Chunk chunk) {
        try(ProfileFrame ignore = platform.getProfiler().profile("feature")) {
            int cx = chunk.getX() << 4;
            int cz = chunk.getZ() << 4;
            long seed = world.getSeed();
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    int tx = cx + x;
                    int tz = cz + z;
                    ColumnImpl column = new ColumnImpl(tx, tz, world);
                    world.getBiomeProvider().getBiome(tx, tz, seed).getContext().get(BiomeFeatures.class).getFeatures().forEach(feature -> {
                        if(feature.getDistributor().matches(tx, tz, seed)) {
                            feature.getLocator()
                                   .getSuitableCoordinates(column)
                                   .forEach(y ->
                                                    feature.getStructure(world, tx, y, tz)
                                                           .generate(new Vector3(tx, y, tz), world, PopulationUtil.getRandom(chunk),
                                                                     Rotation.NONE)
                                           );
                        }
                    });
                }
            }
        }
    }
}
