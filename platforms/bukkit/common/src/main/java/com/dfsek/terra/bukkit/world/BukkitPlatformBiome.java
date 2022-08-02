package com.dfsek.terra.bukkit.world;

import com.dfsek.terra.api.world.biome.PlatformBiome;

import org.bukkit.block.Biome;


public interface BukkitPlatformBiome extends PlatformBiome {
    Biome getBukkitBiome();
}
