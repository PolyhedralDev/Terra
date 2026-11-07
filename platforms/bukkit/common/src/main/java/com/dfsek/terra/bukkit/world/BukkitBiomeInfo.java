package com.dfsek.terra.bukkit.world;

import dev.wyck.keys.ResourceKey;

import com.dfsek.terra.api.properties.Properties;


public record BukkitBiomeInfo(ResourceKey biomeKey, ResourceKey vanillaKey, org.bukkit.block.Biome bukkitBiome) implements Properties {
}
