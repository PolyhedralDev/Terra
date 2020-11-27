package com.dfsek.terra.config.builder;

import com.dfsek.terra.TerraWorld;
import com.dfsek.terra.biome.grid.UserDefinedGrid;
import org.bukkit.World;
import org.polydev.gaea.biome.Biome;

public class BiomeGridBuilder {
    private double xFreq;
    private double zFreq;

    private Biome[][] biomes;

    public UserDefinedGrid build(World world) {
        return new UserDefinedGrid(world, xFreq, zFreq, biomes, TerraWorld.getWorld(world).getWorldConfig());
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
