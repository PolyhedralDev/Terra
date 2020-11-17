package com.dfsek.terra.structure.spawn;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.generation.ElevationEquation;
import com.dfsek.terra.generation.config.WorldGenerator;
import org.bukkit.World;
import org.polydev.gaea.generation.GenerationPhase;

public class LandSpawn extends Requirement {
    public LandSpawn(World world) {
        super(world);
    }

    @Override
    public boolean matches(int x, int y, int z) {
        TerraWorld tw = TerraWorld.getWorld(getWorld());
        UserDefinedBiome b = (UserDefinedBiome) tw.getGrid().getBiome(x, z, GenerationPhase.POPULATE);
        ElevationEquation elevationEquation = ((WorldGenerator) b.getGenerator()).getElevationEquation();
        int yf = y - ((elevationEquation == null) ? 0 : (int) elevationEquation.getNoise(x, z));
        return b.getGenerator().getNoise(getNoise(), getWorld(), x, yf, z) > 0;
    }
}
