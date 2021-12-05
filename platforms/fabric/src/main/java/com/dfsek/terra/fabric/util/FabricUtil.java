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

import com.mojang.serialization.Lifecycle;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.entity.Container;
import com.dfsek.terra.api.block.entity.MobSpawner;
import com.dfsek.terra.api.block.entity.Sign;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.fabric.mixin.access.BiomeEffectsAccessor;


public final class FabricUtil {
    public static String createBiomeID(ConfigPack pack, String biomeID) {
        return pack.getID().toLowerCase() + "/" + biomeID.toLowerCase(Locale.ROOT);
    }
    
    /**
     * Clones a Vanilla biome and injects Terra data to create a Terra-vanilla biome delegate.
     *
     * @param biome The Terra BiomeBuilder.
     * @param pack  The ConfigPack this biome belongs to.
     *
     * @return The Minecraft delegate biome.
     */
    public static net.minecraft.world.biome.Biome createBiome(Biome biome, ConfigPack pack, DynamicRegistryManager registryManager) {
        // BiomeTemplate template = biome.getTemplate();
        Map<String, Integer> colors = new HashMap<>(); // template.getColors();
        
        //TerraFabricPlugin.FabricAddon fabricAddon = TerraFabricPlugin.getInstance().getFabricAddon();
        
        Registry<net.minecraft.world.biome.Biome> biomeRegistry = registryManager.get(Registry.BIOME_KEY);
        net.minecraft.world.biome.Biome vanilla = ((ProtoPlatformBiome) biome.getPlatformBiome()).get(biomeRegistry);
        
        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
        
        if(pack.vanillaCaves()) {
            for(GenerationStep.Carver carver : GenerationStep.Carver.values()) {
                for(Supplier<ConfiguredCarver<?>> configuredCarverSupplier : vanilla.getGenerationSettings().getCarversForStep(carver)) {
                    generationSettings.carver(carver, configuredCarverSupplier.get());
                }
            }
        }

        /*
        Pair<PreLoadCompatibilityOptions, PostLoadCompatibilityOptions> pair = fabricAddon.getTemplates().get(pack);
        PreLoadCompatibilityOptions compatibilityOptions = pair.getLeft();
        PostLoadCompatibilityOptions postLoadCompatibilityOptions = pair.getRight();

        //TerraFabricPlugin.getInstance().getDebugLogger().info("Injecting Vanilla structures and features into Terra biome " + biome
        .getTemplate().getID());

        Registry<ConfiguredStructureFeature<?, ?>> configuredStructureFeatureRegistry = registryManager.get(Registry
        .CONFIGURED_STRUCTURE_FEATURE_KEY);
        for(Supplier<ConfiguredStructureFeature<?, ?>> structureFeature : vanilla.getGenerationSettings().getStructureFeatures()) {
            Identifier key = configuredStructureFeatureRegistry.getId(structureFeature.get());
            if(!compatibilityOptions.getExcludedBiomeStructures().contains(key) && !postLoadCompatibilityOptions
            .getExcludedPerBiomeStructures().getOrDefault(biome, Collections.emptySet()).contains(key)) {
                generationSettings.structureFeature(structureFeature.get());
                TerraFabricPlugin.getInstance().getDebugLogger().info("Injected structure " + key);
            }
        }

        if(compatibilityOptions.doBiomeInjection()) {
            Registry<ConfiguredFeature<?, ?>> configuredFeatureRegistry = registryManager.get(Registry.CONFIGURED_FEATURE_KEY);
            for(int step = 0; step < vanilla.getGenerationSettings().getFeatures().size(); step++) {
                for(Supplier<ConfiguredFeature<?, ?>> featureSupplier : vanilla.getGenerationSettings().getFeatures().get(step)) {
                    Identifier key = configuredFeatureRegistry.getId(featureSupplier.get());
                    if(!compatibilityOptions.getExcludedBiomeFeatures().contains(key) && !postLoadCompatibilityOptions
                    .getExcludedPerBiomeFeatures().getOrDefault(biome, Collections.emptySet()).contains(key)) {
                        generationSettings.feature(step, featureSupplier);
                        TerraFabricPlugin.getInstance().getDebugLogger().info("Injected feature " + key + " at stage " + step);
                    }
                }
            }
        }

         */
        
        BiomeEffectsAccessor accessor = (BiomeEffectsAccessor) vanilla.getEffects();
        BiomeEffects.Builder effects = new BiomeEffects.Builder()
                .waterColor(colors.getOrDefault("water", accessor.getWaterColor()))
                .waterFogColor(colors.getOrDefault("water-fog", accessor.getWaterFogColor()))
                .fogColor(colors.getOrDefault("fog", accessor.getFogColor()))
                .skyColor(colors.getOrDefault("sky", accessor.getSkyColor()))
                .grassColorModifier(accessor.getGrassColorModifier());
        
        if(colors.containsKey("grass")) {
            effects.grassColor(colors.get("grass"));
        } else {
            accessor.getGrassColor().ifPresent(effects::grassColor);
        }
        if(colors.containsKey("foliage")) {
            effects.foliageColor(colors.get("foliage"));
        } else {
            accessor.getFoliageColor().ifPresent(effects::foliageColor);
        }
        
        return new net.minecraft.world.biome.Biome.Builder()
                .precipitation(vanilla.getPrecipitation())
                .category(vanilla.getCategory())
                .temperature(vanilla.getTemperature())
                .downfall(vanilla.getDownfall())
                .effects(effects.build())
                .spawnSettings(vanilla.getSpawnSettings())
                .generationSettings(generationSettings.build())
                .build();
    }
    
    public static <T> void registerOrOverwrite(Registry<T> registry, RegistryKey<Registry<T>> key, Identifier identifier, T item) {
        if(registry.containsId(identifier)) {
            ((MutableRegistry<T>) registry).set(registry.getRawId(registry.get(identifier)), RegistryKey.of(key, identifier), item,
                                                Lifecycle.stable());
        } else {
            Registry.register(registry, identifier, item);
        }
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
}
