package com.dfsek.terra.structure.spawn;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.genconfig.biome.BiomeConfig;
import org.bukkit.World;
import org.polydev.gaea.generation.GenerationPhase;

public class AirSpawn extends Requirement {
    public AirSpawn(World world) {
        super(world);
    }

    @Override
    public boolean matches(int x, int y, int z) {
        TerraWorld tw = TerraWorld.getWorld(getWorld());
        ConfigPack wc = tw.getConfig();
        UserDefinedBiome b = (UserDefinedBiome) tw.getGrid().getBiome(x, z, GenerationPhase.POPULATE);
        BiomeConfig c = wc.getBiome(b);
        if(y <= c.getOcean().getSeaLevel()) return false;
        return b.getGenerator().getNoise(getNoise(), getWorld(), x, y, z) <= 0;
    }
}
