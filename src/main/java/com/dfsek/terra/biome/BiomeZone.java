package com.dfsek.terra.biome;

import com.dfsek.terra.config.WorldConfig;
import org.bukkit.World;
import org.polydev.gaea.biome.BiomeGrid;
import org.polydev.gaea.math.FastNoise;

import java.util.HashMap;
import java.util.Map;

public class BiomeZone {
    private BiomeGrid[] grids;
    private final World w;
    private final FastNoise noise;
    private static final Map<World, BiomeZone> zones = new HashMap<>();

    public BiomeZone(World w, float freq) {
        this.w = w;
        this.noise = new FastNoise((int) w.getSeed()+2);
        noise.setNoiseType(FastNoise.NoiseType.Value);
        noise.setFrequency(freq);
        setZones(WorldConfig.fromWorld(w).definedGrids);
        zones.put(w, this);
    }

    public void setZones(BiomeGrid[] grids) {
        if(grids.length != 16) throw new IllegalArgumentException("Illegal number of grids!");
        this.grids = grids;
    }

    public BiomeGrid getGrid(int x, int z) {
        return grids[normalize(noise.getValue(x, z))];
    }

    public static BiomeZone fromWorld(World w) {
        if(zones.containsKey(w)) return zones.get(w);
        else return new BiomeZone(w, WorldConfig.fromWorld(w).zoneFreq);
    }

    /**
     * Takes a noise input and normalizes it to a value between 0 and 15 inclusive.
     *
     * @param i - The noise value to normalize.
     * @return int - The normalized value.
     */
    private static int normalize(double i) {
        if(i > 0) i = Math.pow(i, 0.8125); // Redistribute
        else i = -Math.pow(-i, 0.8125); // Redistribute
        return Math.min((int) Math.floor((i+1)*8), 15);
    }
}
