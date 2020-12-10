package com.dfsek.terra.structure.spawn;

import com.dfsek.terra.Terra;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.api.gaea.generation.GenerationPhase;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.generation.config.WorldGenerator;
import org.bukkit.World;

public class LandSpawn extends Requirement {
    public LandSpawn(World world, Terra main) {
        super(world, main);
    }

    @Override
    public boolean matches(int x, int y, int z) {
        TerraWorld tw = main.getWorld(world);
        UserDefinedBiome b = (UserDefinedBiome) tw.getGrid().getBiome(x, z, GenerationPhase.POPULATE);
        double elevation = ((WorldGenerator) b.getGenerator()).getElevation(x, z);
        return b.getGenerator().getNoise(getNoise(), world, x, y, z) + elevation > 0;
    }
}
