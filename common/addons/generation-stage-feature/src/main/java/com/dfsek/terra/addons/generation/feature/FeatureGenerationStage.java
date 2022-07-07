/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.generation.feature;

import java.util.Collections;
import java.util.Random;

import com.dfsek.terra.addons.generation.feature.config.BiomeFeatures;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.properties.PropertyKey;
import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.util.Rotation;
import com.dfsek.terra.api.util.vector.Vector3Int;
import com.dfsek.terra.api.world.WritableWorld;
import com.dfsek.terra.api.world.chunk.generation.ProtoWorld;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;
import com.dfsek.terra.api.world.chunk.generation.util.Column;


public class FeatureGenerationStage implements GenerationStage, StringIdentifiable {
    private final Platform platform;
    
    private final String id;
    
    private final String profile;
    
    private final int resolution;
    private final PropertyKey<BiomeFeatures> biomeFeaturesKey;
    
    public FeatureGenerationStage(Platform platform, String id, int resolution, PropertyKey<BiomeFeatures> biomeFeaturesKey) {
        this.platform = platform;
        this.id = id;
        this.profile = "feature_stage:" + id;
        this.resolution = resolution;
        this.biomeFeaturesKey = biomeFeaturesKey;
    }
    
    @Override
    @SuppressWarnings("try")
    public void populate(ProtoWorld world) {
        platform.getProfiler().push(profile);
        int cx = world.centerChunkX() << 4;
        int cz = world.centerChunkZ() << 4;
        long seed = world.getSeed();
        for(int chunkX = 0; chunkX < 16; chunkX += resolution) {
            for(int chunkZ = 0; chunkZ < 16; chunkZ += resolution) {
                int tx = cx + chunkX;
                int tz = cz + chunkZ;
                world.getBiomeProvider()
                     .getColumn(tx, tz, world)
                     .forRanges(resolution, (min, max, biome) -> {
                         for(int subChunkX = 0; subChunkX < resolution; subChunkX++) {
                             for(int subChunkZ = 0; subChunkZ < resolution; subChunkZ++) {
                                 int x = subChunkX + tx;
                                 int z = subChunkZ + tz;
                                 long coordinateSeed = (seed * 31 + x) * 31 + z;
                                 Column<WritableWorld> column = world.column(x, z);
                                 biome.getContext()
                                      .get(biomeFeaturesKey)
                                      .getFeatures()
                                      .getOrDefault(this, Collections.emptyList())
                                      .forEach(feature -> {
                                          platform.getProfiler().push(feature.getID());
                                          if(feature.getDistributor().matches(x, z, seed)) {
                                              feature.getLocator()
                                                     .getSuitableCoordinates(column.clamp(min, max))
                                                     .forEach(y -> feature.getStructure(world, x, y, z)
                                                                          .generate(Vector3Int.of(x, y, z),
                                                                                    world,
                                                                                    new Random(coordinateSeed * 31 + y),
                                                                                    Rotation.NONE)
                                                             );
                                          }
                                          platform.getProfiler().pop(feature.getID());
                                      });
                             }
                         }
                     });
            }
        }
        platform.getProfiler().pop(profile);
    }
    
    @Override
    public String getID() {
        return id;
    }
}
