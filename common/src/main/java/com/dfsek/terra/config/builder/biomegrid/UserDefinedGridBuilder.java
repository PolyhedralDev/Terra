package com.dfsek.terra.config.builder.biomegrid;

import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.biome.grid.UserDefinedGrid;
import com.dfsek.terra.config.base.ConfigPack;

public class UserDefinedGridBuilder implements BiomeGridBuilder {
    private double xFreq;
    private double zFreq;

    private TerraBiome[][] biomes;

    @Override
    public UserDefinedGrid build(long seed, ConfigPack config) {
        return new UserDefinedGrid(seed, 1D / xFreq, 1D / zFreq, biomes, config);
    }

    public double getXFreq() {
        return xFreq;
    }

    public void setXFreq(double xFreq) {
        this.xFreq = xFreq;
    }

    public double getZFreq() {
        return zFreq;
    }

    public void setZFreq(double zFreq) {
        this.zFreq = zFreq;
    }

    public TerraBiome[][] getBiomes() {
        return biomes;
    }

    public void setBiomes(TerraBiome[][] biomes) {
        this.biomes = biomes;
    }
}
