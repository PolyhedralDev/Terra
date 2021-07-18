package com.dfsek.terra.addons.generation.feature;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.generator.TerraGenerationStage;

public class FeatureGenerationStage implements TerraGenerationStage {
    private final TerraPlugin main;

    public FeatureGenerationStage(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public void populate(World world, Chunk chunk) {
        TerraWorld terraWorld = main.getWorld(world);
        try(ProfileFrame ignore = main.getProfiler().profile("feature")) {
            int cx = chunk.getX() << 4;
            int cz = chunk.getZ() << 4;
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {

                }
            }
        }
    }
}
