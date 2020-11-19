package com.dfsek.terra.structure.spawn;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.genconfig.biome.BiomeConfig;
import com.dfsek.terra.generation.config.WorldGenerator;
import org.bukkit.World;
import org.polydev.gaea.generation.GenerationPhase;

public class OceanSpawn extends Requirement {
    public OceanSpawn(World world) {
        super(world);
    }

    @Override
    public boolean matches(int x, int y, int z) {
        TerraWorld tw = TerraWorld.getWorld(getWorld());
        UserDefinedBiome b = (UserDefinedBiome) tw.getGrid().getBiome(x, z, GenerationPhase.POPULATE);
        BiomeConfig c = tw.getConfig().getBiome(b);
        if(y > c.getOcean().getSeaLevel()) return false;
        int yf = (int) (y - ((WorldGenerator) b.getGenerator()).getElevation(x, z));
        return b.getGenerator().getNoise(getNoise(), getWorld(), x, yf, z) <= 0;
    }
}
