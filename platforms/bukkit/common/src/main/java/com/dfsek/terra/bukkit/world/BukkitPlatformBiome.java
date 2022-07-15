package com.dfsek.terra.bukkit.world;

import org.bukkit.block.Biome;

import com.dfsek.terra.api.world.biome.PlatformBiome;


public interface BukkitPlatformBiome extends PlatformBiome {
    Biome getBukkitBiome();
}
