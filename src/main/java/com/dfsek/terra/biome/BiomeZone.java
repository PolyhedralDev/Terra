package com.dfsek.terra.biome;

import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.base.WorldConfig;
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
    private final BiomeGrid[] grids;
    private final FastNoise noise;
    @Nullable
    private final ImageLoader imageLoader;
    private final boolean useImage;
    private final ImageLoader.Channel channel;

    public BiomeZone(World w, WorldConfig wc, BiomeGrid[] grids) {
        this.noise = new FastNoise((int) w.getSeed()+2);
        this.noise.setNoiseType(FastNoise.NoiseType.SimplexFractal);
        this.noise.setFractalOctaves(4);
        this.noise.setFrequency(wc.getConfig().zoneFreq);
        this.grids = grids;
        imageLoader = wc.imageLoader;
        useImage = wc.fromImage;
        channel = wc.zoneChannel;
    }

    protected BiomeGrid getGrid(int x, int z) {
        return grids[NormalizationUtil.normalize(useImage ? Objects.requireNonNull(imageLoader).getNoiseVal(x, z, channel) : noise.getNoise(x, z), grids.length)];
    }

    public int getSize() {
        return grids.length;
    }

    public int getNoise(int x, int z) {
        return NormalizationUtil.normalize(useImage ? Objects.requireNonNull(imageLoader).getNoiseVal(x, z, channel) : noise.getNoise(x, z), grids.length);
    }

    public double getRawNoise(int x, int z) {
        return useImage ? Objects.requireNonNull(imageLoader).getNoiseVal(x, z, channel) : noise.getNoise(x, z);
    }
}
