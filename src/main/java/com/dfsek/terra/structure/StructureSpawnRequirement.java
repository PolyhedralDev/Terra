package com.dfsek.terra.structure;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.genconfig.biome.BiomeConfig;
import org.bukkit.World;
import org.polydev.gaea.generation.GenerationPhase;
import org.polydev.gaea.math.FastNoiseLite;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public enum StructureSpawnRequirement implements Serializable {
    AIR {
        @Override
        public boolean matches(World w, int x, int y, int z) {
            setNoise(w, x, y, z);
            TerraWorld tw = TerraWorld.getWorld(w);
            ConfigPack wc = tw.getConfig();
            UserDefinedBiome b = (UserDefinedBiome) tw.getGrid().getBiome(x, z, GenerationPhase.POPULATE);
            BiomeConfig c = wc.getBiome(b);
            if (y <= c.getOcean().getSeaLevel()) return false;
            return b.getGenerator().getNoise(getNoise(w), w, x, y, z) <= 0;
        }
    }, OCEAN {
        @Override
        public boolean matches(World w, int x, int y, int z) {
            setNoise(w, x, y, z);
            UserDefinedBiome b = (UserDefinedBiome) TerraWorld.getWorld(w).getGrid().getBiome(x, z, GenerationPhase.POPULATE);
            BiomeConfig c = TerraWorld.getWorld(w).getConfig().getBiome(b);
            if (y > c.getOcean().getSeaLevel()) return false;
            return b.getGenerator().getNoise(getNoise(w), w, x, y, z) <= 0;
        }
    }, LAND {
        @Override
        public boolean matches(World w, int x, int y, int z) {
            setNoise(w, x, y, z);
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
    private static final transient Map<World, FastNoiseLite> noiseMap = new HashMap<>();

    private static void setNoise(World w, int x, int y, int z) {
        TerraWorld tw = TerraWorld.getWorld(w);
        ConfigPack wc = tw.getConfig();
        if (getNoise(w) == null) {
            FastNoiseLite gen = new FastNoiseLite((int) w.getSeed());
            gen.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
            gen.setFractalType(FastNoiseLite.FractalType.FBm);
            gen.setFractalOctaves(wc.octaves);
            gen.setFrequency(wc.frequency);
            putNoise(w, gen);
        }
    }

    public static void putNoise(World w, FastNoiseLite noise) {
        noiseMap.putIfAbsent(w, noise);
    }

    private static FastNoiseLite getNoise(World w) {
        return noiseMap.get(w);
    }

    public abstract boolean matches(World w, int x, int y, int z);
}
