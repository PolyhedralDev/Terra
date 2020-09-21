package com.dfsek.terra.biome;

import com.dfsek.terra.config.WorldConfig;
import org.bukkit.World;
import org.polydev.gaea.biome.BiomeGrid;
import org.polydev.gaea.biome.NormalizationUtil;
import org.polydev.gaea.math.FastNoise;

import java.util.HashMap;
import java.util.Map;

public class BiomeZone {
    private BiomeGrid[] grids;
    private final World w;
    private final FastNoise noise;
    private static final Map<World, BiomeZone> zones = new HashMap<>();
    private BiomeZone(World w, float freq) {
        this.w = w;
        this.noise = new FastNoise((int) w.getSeed()+2);
        this.noise.setNoiseType(FastNoise.NoiseType.SimplexFractal);
        this.noise.setFractalOctaves(4);
        this.noise.setFrequency(WorldConfig.fromWorld(w).zoneFreq);
        setZones(WorldConfig.fromWorld(w).definedGrids);
        zones.put(w, this);
    }

    public void setZones(BiomeGrid[] grids) {
        if(grids.length != 32) throw new IllegalArgumentException("Illegal number of grids!");
        this.grids = grids;
    }

    protected BiomeGrid getGrid(int x, int z) {
        return grids[NormalizationUtil.normalize(noise.getNoise(x, z), 32)];
    }

    protected static BiomeZone fromWorld(World w) {
        if(zones.containsKey(w)) return zones.get(w);
        else return new BiomeZone(w, WorldConfig.fromWorld(w).zoneFreq);
    }
}
