package com.dfsek.terra.biome;

import com.dfsek.terra.config.WorldConfig;
import com.dfsek.terra.config.genconfig.BiomeGridConfig;
import com.dfsek.terra.image.ImageLoader;
import org.bukkit.Location;
import org.bukkit.World;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.biome.BiomeGrid;

public class UserDefinedGrid extends BiomeGrid {
    private final ImageLoader imageLoader;
    private final boolean fromImage;
    private final ImageLoader.Channel channelX;
    private final ImageLoader.Channel channelZ;
    public UserDefinedGrid(World w, float freq1, float freq2, BiomeGridConfig config) {
        super(w, freq1, freq2, config.getBiomeGrid().length, config.getBiomeGrid()[0].length);
        super.setNormalType(NormalType.LOOKUP4096);
        super.setGrid(config.getBiomeGrid());
        WorldConfig c = WorldConfig.fromWorld(w);
        imageLoader = c.imageLoader;
        fromImage = c.fromImage;
        channelX = c.biomeXChannel;
        channelZ = c.biomeZChannel;
    }
    public UserDefinedGrid(World w, float freq1, float freq2, UserDefinedBiome[][] b) {
        super(w, freq1, freq2, b.length, b[0].length);
        super.setNormalType(NormalType.LOOKUP4096);
        super.setGrid(b);
        WorldConfig c = WorldConfig.fromWorld(w);
        imageLoader = c.imageLoader;
        fromImage = c.fromImage;
        channelX = c.biomeXChannel;
        channelZ = c.biomeZChannel;
    }

    @Override
    public Biome getBiome(int x, int z) {
        if(fromImage) {
            int xi = imageLoader.getChannel(x, z, channelX);
            int zi = imageLoader.getChannel(x, z, channelZ);
            return super.getGrid()[getSizeX() * (xi/256)][getSizeZ() * (zi/256)];
        }
        return super.getBiome(x, z);
    }

    @Override
    public Biome getBiome(Location l) {
        return this.getBiome(l.getBlockX(), l.getBlockZ());
    }
}
