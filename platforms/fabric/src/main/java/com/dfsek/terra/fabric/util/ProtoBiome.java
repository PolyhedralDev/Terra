package com.dfsek.terra.fabric.util;

import com.dfsek.terra.api.platform.world.Biome;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ProtoBiome implements Biome {
    private final Identifier identifier;

    public ProtoBiome(Identifier identifier) {
        this.identifier = identifier;
    }

    public net.minecraft.world.biome.Biome get(Registry<net.minecraft.world.biome.Biome> registry) {
        return registry.get(identifier);
    }

    @Override
    public Object getHandle() {
        return identifier;
    }
}
