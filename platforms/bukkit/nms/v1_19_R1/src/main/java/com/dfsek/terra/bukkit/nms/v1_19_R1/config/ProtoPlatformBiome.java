/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.bukkit.nms.v1_19_R1.config;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.bukkit.nms.v1_19_R1.util.MinecraftUtil;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;


public class ProtoPlatformBiome implements PlatformBiome, BukkitPlatformBiome {
    private final ResourceLocation identifier;
    
    private final ResourceKey<Biome> biome;
    
    public ProtoPlatformBiome(ResourceLocation identifier, ResourceKey<Biome> biome) {
        this.identifier = identifier;
        this.biome = biome;
    }
    
    public ResourceKey<Biome> get(Registry<Biome> registry) {
        return MinecraftUtil.getEntry(registry, identifier).orElseThrow().unwrapKey().orElseThrow();
    }
    
    @Override
    public Object getHandle() {
        return identifier;
    }
    
    public ResourceKey<Biome> getBiome() {
        return biome;
    }
    
    @Override
    public org.bukkit.block.Biome getBukkitBiome() {
        return org.bukkit.block.Biome.CUSTOM;
    }
}
