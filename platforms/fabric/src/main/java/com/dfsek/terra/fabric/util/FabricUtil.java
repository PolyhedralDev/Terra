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

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome.Builder;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.entity.Container;
import com.dfsek.terra.api.block.entity.MobSpawner;
import com.dfsek.terra.api.block.entity.Sign;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.fabric.FabricEntryPoint;
import com.dfsek.terra.fabric.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.fabric.config.VanillaBiomeProperties;


public final class FabricUtil {
    private static final Logger logger = LoggerFactory.getLogger(FabricUtil.class);
    private static final Map<Identifier, List<Identifier>>
            TERRA_BIOME_MAP = new HashMap<>();
    
    public static String createBiomeID(ConfigPack pack, com.dfsek.terra.api.registry.key.RegistryKey biomeID) {
        return pack.getID()
                   .toLowerCase() + "/" + biomeID.getNamespace().toLowerCase(Locale.ROOT) + "/" + biomeID.getID().toLowerCase(Locale.ROOT);
    }
    
    public static void registerBiomes() {
        logger.info("Registering biomes...");
        FabricEntryPoint.getPlatform().getConfigRegistry().forEach(pack -> { // Register all Terra biomes.
            pack.getCheckedRegistry(Biome.class)
                .forEach((id, biome) -> registerBiome(biome, pack, id));
        });
    }
    
    /**
     * Clones a Vanilla biome and injects Terra data to create a Terra-vanilla biome delegate.
     *
     * @param biome The Terra BiomeBuilder.
     * @param pack  The ConfigPack this biome belongs to.
     */
    public static void registerBiome(Biome biome, ConfigPack pack,
                                     com.dfsek.terra.api.registry.key.RegistryKey id) {
        Registry<net.minecraft.world.biome.Biome> registry = BuiltinRegistries.BIOME;
        RegistryKey<net.minecraft.world.biome.Biome> vanilla = ((ProtoPlatformBiome) biome.getPlatformBiome()).get(registry);
        
        
        if(pack.getContext().get(PreLoadCompatibilityOptions.class).useVanillaBiomes()) {
            ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(vanilla);
        } else {
            net.minecraft.world.biome.Biome minecraftBiome = createBiome(biome, registry.get(vanilla));
            
            Identifier identifier = new Identifier("terra", FabricUtil.createBiomeID(pack, id));
            
            if(registry.containsId(identifier)) {
                ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(FabricUtil.getEntry(registry, identifier)
                                                                                      .orElseThrow()
                                                                                      .getKey()
                                                                                      .orElseThrow());
            } else {
                ((ProtoPlatformBiome) biome.getPlatformBiome()).setDelegate(BuiltinRegistries.add(registry,
                                                                                                  registerKey(identifier).getValue(),
                                                                                                  minecraftBiome).getKey().orElseThrow());
            }
            
            TERRA_BIOME_MAP.computeIfAbsent(vanilla.getValue(), i -> new ArrayList<>()).add(identifier);
        }
    }
    
    private static RegistryKey<net.minecraft.world.biome.Biome> registerKey(Identifier identifier) {
        return RegistryKey.of(Registry.BIOME_KEY, identifier);
    }
    
    public static void registerTags(Registry<net.minecraft.world.biome.Biome> registry) {
        logger.info("Doing tag garbage....");
        Map<TagKey<net.minecraft.world.biome.Biome>, List<RegistryEntry<net.minecraft.world.biome.Biome>>> collect = registry
                .streamTagsAndEntries()
                .collect(HashMap::new,
                         (map, pair) ->
                                 map.put(pair.getFirst(), new ArrayList<>(pair.getSecond().stream().toList())),
                         HashMap::putAll);
        
        TERRA_BIOME_MAP.forEach((vb, terraBiomes) -> getEntry(registry, vb)
                .ifPresentOrElse(vanilla -> terraBiomes.forEach(tb -> getEntry(registry, tb)
                                         .ifPresentOrElse(
                                                 terra -> {
                                                     logger.debug(vanilla.getKey().orElseThrow().getValue() + " (vanilla for " +
                                                                  terra.getKey().orElseThrow().getValue() + ": " +
                                                                  vanilla.streamTags().toList());
                                    
                                                     vanilla.streamTags()
                                                            .forEach(
                                                                    tag -> collect
                                                                            .computeIfAbsent(tag, t -> new ArrayList<>())
                                                                            .add(getEntry(registry,
                                                                                          terra.getKey()
                                                                                               .orElseThrow()
                                                                                               .getValue()).orElseThrow()));
                                    
                                                 },
                                                 () -> logger.error("No such biome: {}", tb))),
                                 () -> logger.error("No vanilla biome: {}", vb)));
        
        registry.clearTags();
        registry.populateTags(ImmutableMap.copyOf(collect));
        
        if(logger.isDebugEnabled()) {
            registry.streamEntries()
                    .map(e -> e.registryKey().getValue() + ": " +
                              e.streamTags().reduce("", (s, t) -> t.id() + ", " + s, String::concat))
                    .forEach(logger::debug);
        }
    }
    
    public static net.minecraft.world.biome.Biome createBiome(Biome biome, net.minecraft.world.biome.Biome vanilla) {
        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
        
        BiomeEffects.Builder effects = new BiomeEffects.Builder();
        
        net.minecraft.world.biome.Biome.Builder builder = new Builder();
        
        if(biome.getContext().has(VanillaBiomeProperties.class)) {
            VanillaBiomeProperties vanillaBiomeProperties = biome.getContext().get(VanillaBiomeProperties.class);
            
            effects.waterColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterColor(), vanilla.getWaterColor()))
                   .waterFogColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterFogColor(), vanilla.getWaterFogColor()))
                   .fogColor(Objects.requireNonNullElse(vanillaBiomeProperties.getFogColor(), vanilla.getFogColor()))
                   .skyColor(Objects.requireNonNullElse(vanillaBiomeProperties.getSkyColor(), vanilla.getSkyColor()))
                   .grassColorModifier(
                           Objects.requireNonNullElse(vanillaBiomeProperties.getModifier(), vanilla.getEffects().getGrassColorModifier()));
            
            
            if(vanillaBiomeProperties.getGrassColor() == null) {
                vanilla.getEffects().getGrassColor().ifPresent(effects::grassColor);
            } else {
                effects.grassColor(vanillaBiomeProperties.getGrassColor());
            }
            
            if(vanillaBiomeProperties.getFoliageColor() == null) {
                vanilla.getEffects().getFoliageColor().ifPresent(effects::foliageColor);
            } else {
                effects.foliageColor(vanillaBiomeProperties.getFoliageColor());
            }
            
            
            builder.precipitation(Objects.requireNonNullElse(vanillaBiomeProperties.getPrecipitation(), vanilla.getPrecipitation()))
                   .category(Objects.requireNonNullElse(vanillaBiomeProperties.getCategory(), vanilla.getCategory()));
            
        } else {
            
            effects.waterColor(vanilla.getWaterColor())
                   .waterFogColor(vanilla.getWaterFogColor())
                   .fogColor(vanilla.getFogColor())
                   .skyColor(vanilla.getSkyColor());
            vanilla.getEffects().getFoliageColor().ifPresent(effects::foliageColor);
            vanilla.getEffects().getGrassColor().ifPresent(effects::grassColor);
            
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
                       .map(registry::getOrCreateEntry);
    }
}
