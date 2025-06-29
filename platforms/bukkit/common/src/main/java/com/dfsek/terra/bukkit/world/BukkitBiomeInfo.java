package com.dfsek.terra.bukkit.world;

import org.bukkit.NamespacedKey;

import com.dfsek.terra.api.properties.Properties;


public record BukkitBiomeInfo(NamespacedKey biomeKey) implements Properties {}
