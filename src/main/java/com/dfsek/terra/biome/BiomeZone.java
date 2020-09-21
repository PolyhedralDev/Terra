package com.dfsek.terra.biome;

import com.dfsek.terra.config.WorldConfig;
import com.dfsek.terra.image.ImageLoader;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.polydev.gaea.biome.BiomeGrid;
import org.polydev.gaea.biome.NormalizationUtil;
import org.polydev.gaea.math.FastNoise;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BiomeZone {
    private BiomeGrid[] grids;
    private final World w;
    private final FastNoise noise;
    private static final Map<World, BiomeZone> zones = new HashMap<>();
    @Nullable
    private final ImageLoader imageLoader;
    private final boolean useImage;
    private final ImageLoader.Channel channel;
    private BiomeZone(World w, float freq) {
        this.w = w;
        this.noise = new FastNoise((int) w.getSeed()+2);
        this.noise.setNoiseType(FastNoise.NoiseType.SimplexFractal);
        this.noise.setFractalOctaves(4);
        this.noise.setFrequency(WorldConfig.fromWorld(w).zoneFreq);
        WorldConfig c = WorldConfig.fromWorld(w);
        setZones(c.definedGrids);
        imageLoader = c.imageLoader;
        useImage = c.fromImage;
        channel = c.zoneChannel;
        zones.put(w, this);
    }

    public void setZones(@NotNull BiomeGrid[] grids) {
        if(grids.length != 32) throw new IllegalArgumentException("Illegal number of grids!");
        this.grids = grids;
    }

    protected BiomeGrid getGrid(int x, int z) {
        return grids[NormalizationUtil.normalize(useImage ? Objects.requireNonNull(imageLoader).getNoiseVal(x, z, channel) : noise.getNoise(x, z), 32)];
    }

    protected static BiomeZone fromWorld(World w) {
        if(zones.containsKey(w)) return zones.get(w);
        else return new BiomeZone(w, WorldConfig.fromWorld(w).zoneFreq);
    }
}
