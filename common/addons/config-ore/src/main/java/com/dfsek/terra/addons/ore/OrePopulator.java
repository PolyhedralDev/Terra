/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.ore;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.util.PopulationUtil;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.generator.GenerationStage;


public class OrePopulator implements GenerationStage {
    private final Platform platform;
    
    public OrePopulator(Platform platform) {
        this.platform = platform;
    }
    
    @SuppressWarnings("try")
    @Override
    public void populate(@NotNull World world, @NotNull Chunk chunk) {
        try(ProfileFrame ignore = platform.getProfiler().profile("ore")) {
            if(world.getConfig().disableOres()) return;
            
            for(int cx = -1; cx <= 1; cx++) {
                for(int cz = -1; cz <= 1; cz++) {
                    Random random = new Random(PopulationUtil.getCarverChunkSeed(chunk.getX() + cx, chunk.getZ() + cz, world.getSeed()));
                    int originX = ((chunk.getX() + cx) << 4);
                    int originZ = ((chunk.getZ() + cz) << 4);
                    TerraBiome b = world.getBiomeProvider().getBiome(originX + 8, originZ + 8, world.getSeed());
                    /*
                    BiomeTemplate config = ((UserDefinedBiome) b).getConfig();
                    int finalCx = cx;
                    int finalCz = cz;
                    config.getOreHolder().forEach((id, orePair) -> {
                        try(ProfileFrame ignored = main.getProfiler().profile("ore:" + id)) {
                            int amount = orePair.getRight().getAmount().get(random);
                            for(int i = 0; i < amount; i++) {
                                Vector3 location = new Vector3(random.nextInt(16) + 16 * finalCx, orePair.getRight().getHeight().get
                                (random), random.nextInt(16) + 16 * finalCz);
                                orePair.getLeft().generate(location, chunk, random);
                            }
                        }
                    });

                     */
                }
            }
        }
    }
}
