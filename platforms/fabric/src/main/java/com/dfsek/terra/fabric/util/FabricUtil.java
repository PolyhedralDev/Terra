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

package com.dfsek.terra.fabric.util;

import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome.Builder;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.entity.Container;
import com.dfsek.terra.api.block.entity.MobSpawner;
import com.dfsek.terra.api.block.entity.Sign;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.fabric.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.fabric.config.VanillaBiomeProperties;
import com.dfsek.terra.fabric.mixin.access.BiomeEffectsAccessor;


public final class FabricUtil {
    public static String createBiomeID(ConfigPack pack, com.dfsek.terra.api.registry.key.RegistryKey biomeID) {
        return pack.getID()
                   .toLowerCase() + "/" + biomeID.getNamespace().toLowerCase(Locale.ROOT) + "/" + biomeID.getID().toLowerCase(Locale.ROOT);
    }
    
    /**
     * Clones a Vanilla biome and injects Terra data to create a Terra-vanilla biome delegate.
     *
     * @param biome The Terra BiomeBuilder.
     * @param pack  The ConfigPack this biome belongs to.
     */
    public static void registerBiome(Biome biome, ConfigPack pack, DynamicRegistryManager registryManager,
                                     com.dfsek.terra.api.registry.key.RegistryKey id) {
        Registry<net.minecraft.world.biome.Biome> biomeRegistry = registryManager.get(Registry.BIOME_KEY);
        RegistryEntry<net.minecraft.world.biome.Biome> vanilla = ((ProtoPlatformBiome) biome.getPlatformBiome()).get(biomeRegistry);
        
        
        if(pack.getContext().get(PreLoadCompatibilityOptions.class).useVanillaBiomes()) {
            ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(vanilla);
        } else {
            net.minecraft.world.biome.Biome minecraftBiome = createBiome(biome, vanilla.value());
            
            Identifier identifier = new Identifier("terra", FabricUtil.createBiomeID(pack, id));
            
            if(biomeRegistry.containsId(identifier)) {
                ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(FabricUtil.getEntry(biomeRegistry, identifier).orElseThrow());
            } else {
                Registry.register(biomeRegistry, identifier, minecraftBiome);
                ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(FabricUtil.getEntry(biomeRegistry, identifier).orElseThrow());
            }
        }
    }
    
    public static net.minecraft.world.biome.Biome createBiome(Biome biome, net.minecraft.world.biome.Biome vanilla) {
        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
        
        BiomeEffectsAccessor accessor = (BiomeEffectsAccessor) vanilla.getEffects();
        BiomeEffects.Builder effects = new BiomeEffects.Builder();
        
        
        net.minecraft.world.biome.Biome.Builder builder = new Builder();
        
        if(biome.getContext().has(VanillaBiomeProperties.class)) {
            VanillaBiomeProperties vanillaBiomeProperties = biome.getContext().get(VanillaBiomeProperties.class);
            
            effects.waterColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterColor(), vanilla.getWaterColor()))
                   .waterFogColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterFogColor(), vanilla.getWaterFogColor()))
                   .fogColor(Objects.requireNonNullElse(vanillaBiomeProperties.getFogColor(), vanilla.getFogColor()))
                   .skyColor(Objects.requireNonNullElse(vanillaBiomeProperties.getSkyColor(), vanilla.getSkyColor()))
                   .grassColorModifier(Objects.requireNonNullElse(vanillaBiomeProperties.getModifier(), accessor.getGrassColorModifier()));
            
            
            if(vanillaBiomeProperties.getGrassColor() == null) {
                accessor.getGrassColor().ifPresent(effects::grassColor);
            } else {
                effects.grassColor(vanillaBiomeProperties.getGrassColor());
            }
            
            if(vanillaBiomeProperties.getFoliageColor() == null) {
                accessor.getFoliageColor().ifPresent(effects::foliageColor);
            } else {
                effects.foliageColor(vanillaBiomeProperties.getFoliageColor());
            }
            
            
            builder.precipitation(Objects.requireNonNullElse(vanillaBiomeProperties.getPrecipitation(), vanilla.getPrecipitation()))
                   .category(Objects.requireNonNullElse(vanillaBiomeProperties.getCategory(), vanilla.getCategory()));
            
        } else {
            effects.waterColor(accessor.getWaterColor())
                   .waterFogColor(accessor.getWaterFogColor())
                   .fogColor(accessor.getFogColor())
                   .skyColor(accessor.getSkyColor());
            accessor.getFoliageColor().ifPresent(effects::foliageColor);
            accessor.getGrassColor().ifPresent(effects::grassColor);
            
            builder.precipitation(vanilla.getPrecipitation())
                   .category(vanilla.getCategory());
        }
        
        return builder
                .temperature(vanilla.getTemperature())
                .downfall(vanilla.getDownfall())
                .effects(effects.build())
                .spawnSettings(vanilla.getSpawnSettings())
                .generationSettings(generationSettings.build())
                .build();
    }
    
    public static BlockEntity createState(WorldAccess worldAccess, BlockPos pos) {
        net.minecraft.block.entity.BlockEntity entity = worldAccess.getBlockEntity(pos);
        if(entity instanceof SignBlockEntity) {
            return (Sign) entity;
        } else if(entity instanceof MobSpawnerBlockEntity) {
            return (MobSpawner) entity;
        } else if(entity instanceof LootableContainerBlockEntity) {
            return (Container) entity;
        }
        return null;
    }
    
    public static <T> Optional<RegistryEntry<T>> getEntry(Registry<T> registry, Identifier identifier) {
        return registry.getOrEmpty(identifier)
                       .flatMap(registry::getKey)
                       .flatMap(registry::getEntry);
    }
}
