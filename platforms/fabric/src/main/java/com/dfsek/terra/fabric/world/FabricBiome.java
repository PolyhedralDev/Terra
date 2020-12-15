package com.dfsek.terra.fabric.world;

import com.dfsek.terra.api.generic.world.Biome;

public class FabricBiome implements Biome {
    private final net.minecraft.world.biome.Biome delegate;

    public FabricBiome(net.minecraft.world.biome.Biome delegate) {
        this.delegate = delegate;
    }


    @Override
    public net.minecraft.world.biome.Biome getHandle() {
        return delegate;
    }
}
