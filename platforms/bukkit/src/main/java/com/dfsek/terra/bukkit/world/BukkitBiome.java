package com.dfsek.terra.bukkit.world;

import com.dfsek.terra.api.world.biome.Biome;

public class BukkitBiome implements Biome {
    private final org.bukkit.block.Biome biome;

    public BukkitBiome(org.bukkit.block.Biome biome) {
        this.biome = biome;
    }

    @Override
    public org.bukkit.block.Biome getHandle() {
        return biome;
    }
}
