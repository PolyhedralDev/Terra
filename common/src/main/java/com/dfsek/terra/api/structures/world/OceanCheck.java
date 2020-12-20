package com.dfsek.terra.api.structures.world;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.world.generation.GenerationPhase;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.generation.config.WorldGenerator;

public class OceanCheck extends SpawnCheck {
    public OceanCheck(World world, TerraPlugin main) {
        super(world, main);
    }

    @Override
    public boolean check(int x, int y, int z) {
        TerraWorld tw = main.getWorld(world);
        UserDefinedBiome b = (UserDefinedBiome) tw.getGrid().getBiome(x, z, GenerationPhase.POPULATE);
        BiomeTemplate c = b.getConfig();
        if(y > c.getSeaLevel()) return false;
        double elevation = ((WorldGenerator) b.getGenerator()).getElevation(x, z);
        return b.getGenerator().getNoise(world, x, y, z) + elevation <= 0;
    }
}
