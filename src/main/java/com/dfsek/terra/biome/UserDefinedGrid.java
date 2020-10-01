package com.dfsek.terra.biome;

import com.dfsek.terra.config.base.WorldConfig;
import com.dfsek.terra.image.ImageLoader;
import org.bukkit.Location;
import org.bukkit.World;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.biome.BiomeGrid;
import org.polydev.gaea.biome.NormalizationUtil;
import org.polydev.gaea.generation.GenerationPhase;

public class UserDefinedGrid extends BiomeGrid {
    private final ImageLoader imageLoader;
    private final boolean fromImage;
    private final ImageLoader.Channel channelX;
    private final ImageLoader.Channel channelZ;
    public UserDefinedGrid(World w, float freq1, float freq2, UserDefinedBiome[][] b, WorldConfig c) {
        super(w, freq1, freq2, b.length, b[0].length);
        super.setNormalType(NormalType.LOOKUP4096);
        super.setGrid(b);
        imageLoader = c.imageLoader;
        fromImage = c.fromImage;
        channelX = c.biomeXChannel;
        channelZ = c.biomeZChannel;
    }

    @Override
    public Biome getBiome(int x, int z, GenerationPhase phase) {
        if(fromImage) {
            double xi = imageLoader.getNoiseVal(x, z, channelX);
            double zi = imageLoader.getNoiseVal(x, z, channelZ);
            return super.getGrid()[NormalizationUtil.normalize(xi, getSizeX())][NormalizationUtil.normalize(zi, getSizeZ())];
        }
        return super.getBiome(x, z, phase);
    }

    @Override
    public Biome getBiome(Location l, GenerationPhase phase) {
        return this.getBiome(l.getBlockX(), l.getBlockZ(), phase);
    }
}
