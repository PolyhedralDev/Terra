package com.dfsek.terra.structure.spawn;

import com.dfsek.terra.Terra;
import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.generation.config.WorldGenerator;
import org.bukkit.World;
import org.polydev.gaea.generation.GenerationPhase;

public class OceanSpawn extends Requirement {
    public OceanSpawn(World world, Terra main) {
        super(world, main);
    }

    @Override
    public boolean matches(int x, int y, int z) {
        TerraWorld tw = main.getWorld(world);
        UserDefinedBiome b = (UserDefinedBiome) tw.getGrid().getBiome(x, z, GenerationPhase.POPULATE);
        BiomeTemplate c = b.getConfig();
        if(y > c.getSeaLevel()) return false;
        double elevation = ((WorldGenerator) b.getGenerator()).getElevation(x, z);
        return b.getGenerator().getNoise(getNoise(), world, x, y, z) + elevation <= 0;
    }
}
