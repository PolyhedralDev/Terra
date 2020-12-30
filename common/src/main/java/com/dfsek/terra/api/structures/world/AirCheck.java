package com.dfsek.terra.api.structures.world;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.world.generation.GenerationPhase;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.grid.master.TerraBiomeGrid;
import com.dfsek.terra.config.templates.BiomeTemplate;

public class AirCheck extends SpawnCheck {
    public AirCheck(World world, TerraPlugin main) {
        super(world, main);
    }

    @Override
    public boolean check(int x, int y, int z) {
        TerraWorld tw = main.getWorld(world);
        TerraBiomeGrid g = tw.getGrid();
        UserDefinedBiome b = (UserDefinedBiome) g.getBiome(x, z, GenerationPhase.POPULATE);
        BiomeTemplate c = b.getConfig();
        if(y <= c.getSeaLevel()) return false;
        return sample(x, y, z, g) <= 0;
    }
}
