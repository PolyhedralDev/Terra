package com.dfsek.terra.biome.grid;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.world.biome.BiomeGrid;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.generation.GenerationPhase;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.ConfigPackTemplate;
import com.dfsek.terra.image.ImageLoader;

public class UserDefinedGrid extends BiomeGrid {
    private final ImageLoader imageLoader;
    private final boolean fromImage;
    private final ImageLoader.Channel channelX;
    private final ImageLoader.Channel channelZ;

    public UserDefinedGrid(long seed, double freq1, double freq2, TerraBiome[][] b, ConfigPack c) {
        super(seed, freq1, freq2, b.length, b[0].length);
        super.setGrid(b);
        ConfigPackTemplate t = c.getTemplate();
        imageLoader = t.getImageLoader();
        fromImage = t.isFromImage();
        channelX = t.getBiomeXChannel();
        channelZ = t.getBiomeZChannel();
    }

    @Override
    public TerraBiome getBiome(int x, int z, GenerationPhase phase) {
        if(fromImage) {
            int xi = imageLoader.getNoiseVal(x, z, getSizeX() - 1, channelX);
            int zi = imageLoader.getNoiseVal(x, z, getSizeZ() - 1, channelZ);
            return super.getGrid()[xi][zi];
        }
        return super.getBiome(x, z, phase);
    }

    @Override
    public TerraBiome getBiome(Location l, GenerationPhase phase) {
        return this.getBiome(l.getBlockX(), l.getBlockZ(), phase);
    }
}
