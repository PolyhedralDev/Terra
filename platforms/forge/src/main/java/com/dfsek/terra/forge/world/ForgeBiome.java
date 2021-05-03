package com.dfsek.terra.forge.world;

import com.dfsek.terra.api.platform.world.Biome;

public class ForgeBiome implements Biome {
    private final net.minecraft.world.biome.Biome delegate;

    public ForgeBiome(net.minecraft.world.biome.Biome delegate) {
        this.delegate = delegate;
    }


    @Override
    public net.minecraft.world.biome.Biome getHandle() {
        return delegate;
    }
}
