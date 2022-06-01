package com.dfsek.terra.bukkit.nms.v1_18_R2;

import com.dfsek.terra.api.properties.Properties;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.BiomeBase;


public record NMSBiomeInfo(ResourceKey<BiomeBase> biomeKey) implements Properties {
}
