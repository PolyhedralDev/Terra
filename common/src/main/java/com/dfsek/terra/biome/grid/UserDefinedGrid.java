package com.dfsek.terra.biome.grid;

import com.dfsek.terra.api.gaea.biome.Biome;
import com.dfsek.terra.api.gaea.biome.BiomeGrid;
import com.dfsek.terra.api.gaea.biome.NormalizationUtil;
import com.dfsek.terra.api.gaea.generation.GenerationPhase;
import com.dfsek.terra.api.generic.world.vector.Location;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.ConfigPackTemplate;
import com.dfsek.terra.image.ImageLoader;

public class UserDefinedGrid extends BiomeGrid {
    private final ImageLoader imageLoader;
    private final boolean fromImage;
    private final ImageLoader.Channel channelX;
    private final ImageLoader.Channel channelZ;

    public UserDefinedGrid(long seed, double freq1, double freq2, Biome[][] b, ConfigPack c) {
        super(seed, freq1, freq2, b.length, b[0].length);
        super.setGrid(b);
        ConfigPackTemplate t = c.getTemplate();
        imageLoader = t.getImageLoader();
        fromImage = t.isFromImage();
        channelX = t.getBiomeXChannel();
        channelZ = t.getBiomeZChannel();
    }

    @Override
    public Biome getBiome(int x, int z, GenerationPhase phase) {
        if(fromImage) {
            double xi = imageLoader.getNoiseVal(x, z, channelX);
            double zi = imageLoader.getNoiseVal(x, z, channelZ);
            return super.getGrid()[NormalizationUtil.normalize(xi, getSizeX(), 4)][NormalizationUtil.normalize(zi, getSizeZ(), 4)];
        }
        return super.getBiome(x, z, phase);
    }

    @Override
    public Biome getBiome(Location l, GenerationPhase phase) {
        return this.getBiome(l.getBlockX(), l.getBlockZ(), phase);
    }
}
