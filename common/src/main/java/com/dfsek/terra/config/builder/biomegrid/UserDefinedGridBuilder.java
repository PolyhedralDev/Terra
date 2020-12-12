package com.dfsek.terra.config.builder.biomegrid;

import com.dfsek.terra.api.gaea.biome.Biome;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.biome.grid.UserDefinedGrid;
import com.dfsek.terra.config.base.ConfigPack;

public class UserDefinedGridBuilder implements BiomeGridBuilder {
    private double xFreq;
    private double zFreq;

    private Biome[][] biomes;

    @Override
    public UserDefinedGrid build(World world, ConfigPack config) {
        return new UserDefinedGrid(world, 1D / xFreq, 1D / zFreq, biomes, config);
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

    public Biome[][] getBiomes() {
        return biomes;
    }

    public void setBiomes(Biome[][] biomes) {
        this.biomes = biomes;
    }
}
