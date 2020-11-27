package com.dfsek.terra.config.builder;

import com.dfsek.terra.biome.grid.UserDefinedGrid;
import com.dfsek.terra.config.base.WorldConfig;
import org.bukkit.World;
import org.polydev.gaea.biome.Biome;

public class BiomeGridBuilder {
    private double xFreq;
    private double zFreq;

    private Biome[][] biomes;

    public UserDefinedGrid build(World world, WorldConfig config) {
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
