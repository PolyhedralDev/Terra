package com.dfsek.terra.structure;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.genconfig.biome.BiomeConfig;
import org.bukkit.Location;
import org.bukkit.World;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.math.FastNoise;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum StructureSpawnRequirement implements Serializable {
    AIR {
        @Override
        public boolean matches(World w, int x, int y, int z) {
            UserDefinedBiome b = (UserDefinedBiome) TerraWorld.getWorld(w).getGrid().getBiome(x, z, GenerationPhase.POPULATE);
            BiomeConfig c = TerraWorld.getWorld(w).getConfig().getBiome(b);
            if(y <= c.getOcean().getSeaLevel()) return false;
            return b.getGenerator().getNoise(getNoise(w), w, x, y, z) <= 0;
        }
    }, OCEAN {
        @Override
        public boolean matches(World w, int x, int y, int z) {
            UserDefinedBiome b = (UserDefinedBiome) TerraWorld.getWorld(w).getGrid().getBiome(x, z, GenerationPhase.POPULATE);
            BiomeConfig c = TerraWorld.getWorld(w).getConfig().getBiome(b);
            if(y > c.getOcean().getSeaLevel()) return false;
            return b.getGenerator().getNoise(getNoise(w), w, x, y, z) <= 0;
        }
    }, LAND {
        @Override
        public boolean matches(World w, int x, int y, int z) {
            UserDefinedBiome b = (UserDefinedBiome) TerraWorld.getWorld(w).getGrid().getBiome(x, z, GenerationPhase.POPULATE);
            return b.getGenerator().getNoise(getNoise(w), w, x, y, z) > 0;
        }
    }, BLANK {
        @Override
        public boolean matches(World w, int x, int y, int z) {
            return true;
        }
    };
    private static final long serialVersionUID = -175639605885943679L;
    private static final transient Map<World, FastNoise> noiseMap = new HashMap<>();
    public abstract boolean matches(World w, int x, int y, int z);
    public static void putNoise(World w, FastNoise noise) {
        noiseMap.putIfAbsent(w, noise);
    }
    private static FastNoise getNoise(World w) {
        return noiseMap.get(w);
    }
}
