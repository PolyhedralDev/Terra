package com.dfsek.terra.structure.spawn;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.generation.config.WorldGenerator;
import org.bukkit.World;
import org.polydev.gaea.generation.GenerationPhase;

public class AirSpawn extends Requirement {
    public AirSpawn(World world) {
        super(world);
    }

    @Override
    public boolean matches(int x, int y, int z) {
        TerraWorld tw = TerraWorld.getWorld(getWorld());
        UserDefinedBiome b = (UserDefinedBiome) tw.getGrid().getBiome(x, z, GenerationPhase.POPULATE);
        BiomeTemplate c = b.getConfig();
        if(y <= c.getSeaLevel()) return false;
        int yf = (int) (y - ((WorldGenerator) b.getGenerator()).getElevation(x, z));
        return b.getGenerator().getNoise(getNoise(), getWorld(), x, yf, z) <= 0;
    }
}
