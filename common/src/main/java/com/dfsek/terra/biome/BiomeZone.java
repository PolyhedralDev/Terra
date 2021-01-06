package com.dfsek.terra.biome;

import com.dfsek.terra.api.math.noise.FastNoiseLite;
import com.dfsek.terra.api.world.biome.BiomeGrid;
import com.dfsek.terra.api.world.biome.NormalizationUtil;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.ConfigPackTemplate;
import com.dfsek.terra.image.ImageLoader;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Holds 1D array of BiomeGrids.
 */
public class BiomeZone {
    private final BiomeGrid[] grids;
    private final FastNoiseLite noise;
    @Nullable
    private final ImageLoader imageLoader;
    private final boolean useImage;
    private final ImageLoader.Channel channel;

    public BiomeZone(long seed, ConfigPack wc, BiomeGrid[] grids) {
        this.noise = new FastNoiseLite((int) seed + 2);
        this.noise.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        this.noise.setFractalType(FastNoiseLite.FractalType.FBm);
        this.noise.setFractalOctaves(4);
        ConfigPackTemplate t = wc.getTemplate();
        this.noise.setFrequency(1D / t.getZoneFreq());
        this.grids = grids;
        imageLoader = t.getImageLoader();
        useImage = t.isFromImage();
        channel = t.getZoneChannel();
    }

    /**
     * Get BiomeGrid at location
     *
     * @param x X coordinate
     * @param z Z coordinate
     * @return BiomeGrid at coordinates.
     */
    public BiomeGrid getGrid(int x, int z) {
        return grids[NormalizationUtil.normalize(useImage ? Objects.requireNonNull(imageLoader).getNoiseVal(x, z, channel) : noise.getNoise(x, z), grids.length, 4)];
    }

    /**
     * Get the number of BiomeGrids this BiomeZone holds.
     *
     * @return Number of grids
     */
    public int getSize() {
        return grids.length;
    }

    /**
     * Get the normalized grid noise at location
     *
     * @param x X coordinate
     * @param z Z coordinate
     * @return Normalized noise at coordinates
     */
    public int getNoise(int x, int z) {
        return NormalizationUtil.normalize(useImage ? Objects.requireNonNull(imageLoader).getNoiseVal(x, z, channel) : noise.getNoise(x, z), grids.length, 4);
    }

    /**
     * Get raw grid noise at location
     *
     * @param x X coordinate
     * @param z Z coordinate
     * @return Raw noise at coordinates
     */
    public double getRawNoise(int x, int z) {
        return useImage ? Objects.requireNonNull(imageLoader).getNoiseVal(x, z, channel) : noise.getNoise(x, z);
    }
}
