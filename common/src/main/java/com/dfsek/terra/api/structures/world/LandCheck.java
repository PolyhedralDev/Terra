package com.dfsek.terra.api.structures.world;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.world.generation.GenerationPhase;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.generation.config.WorldGenerator;

public class LandCheck extends SpawnCheck {
    public LandCheck(World world, TerraPlugin main) {
        super(world, main);
    }

    @Override
    public boolean check(int x, int y, int z) {
        TerraWorld tw = main.getWorld(world);
        UserDefinedBiome b = (UserDefinedBiome) tw.getGrid().getBiome(x, z, GenerationPhase.POPULATE);
        double elevation = ((WorldGenerator) b.getGenerator(world)).getElevation(x, z);
        return b.getGenerator(world).getNoise(x, y, z) + elevation > 0;
    }
}
