package com.dfsek.terra.platform;

import com.dfsek.terra.api.platform.world.Biome;

public class RawBiome implements Biome {
    private final String id;

    public RawBiome(String id) {
        this.id = id;
    }

    @Override
    public Object getHandle() {
        return id;
    }
}
