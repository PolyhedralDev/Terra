package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.platform.world.Biome;

public class MinestomBiome implements Biome {
    private final net.minestom.server.world.biomes.Biome delegate;

    public MinestomBiome(net.minestom.server.world.biomes.Biome delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object getHandle() {
        return delegate;
    }
}
