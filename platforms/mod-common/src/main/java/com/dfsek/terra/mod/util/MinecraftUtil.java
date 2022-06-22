package com.dfsek.terra.mod.util;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.entity.Container;
import com.dfsek.terra.api.block.entity.MobSpawner;
import com.dfsek.terra.api.block.entity.Sign;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.mod.config.VanillaBiomeProperties;
import com.dfsek.terra.mod.mixin_ifaces.FloraFeatureHolder;

import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Builder;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


public final class MinecraftUtil {
    public static final Logger logger = LoggerFactory.getLogger(MinecraftUtil.class);
    public static final Map<Identifier, List<Identifier>>
            TERRA_BIOME_MAP = new HashMap<>();
    
    private MinecraftUtil() {
    
    }
    
    public static <T> Optional<RegistryEntry<T>> getEntry(Registry<T> registry, Identifier identifier) {
        return registry.getOrEmpty(identifier)
                       .flatMap(registry::getKey)
                       .map(registry::getOrCreateEntry);
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
    
    public static void registerFlora(Registry<net.minecraft.world.biome.Biome> biomes) {
        logger.info("Injecting flora into Terra biomes...");
        TERRA_BIOME_MAP
                .forEach((vb, terraBiomes) ->
                                 biomes.getOrEmpty(vb)
                                           .ifPresentOrElse(vanilla -> terraBiomes
                                                                    .forEach(tb -> biomes.getOrEmpty(tb)
                                                                            .ifPresentOrElse(
                                                                                    terra -> {
                                                                                        List<ConfiguredFeature<?, ?>> flowerFeatures = List.copyOf(vanilla.getGenerationSettings().getFlowerFeatures());
                                                                                        logger.debug("Injecting flora into biome {} : {}", tb, flowerFeatures);
                                                                                        ((FloraFeatureHolder) terra.getGenerationSettings()).setFloraFeatures(flowerFeatures);
                                                                                        },
                                                                                    () -> logger.error(
                                                                                            "No such biome: {}",
                                                                                            tb))),
                                                            () -> logger.error("No vanilla biome: {}", vb)));
        
    }
    
    public static Map<Identifier, List<Identifier>> getTerraBiomeMap() {
        return Map.copyOf(TERRA_BIOME_MAP);
    }
    
    public static RegistryKey<Biome> registerKey(Identifier identifier) {
        return RegistryKey.of(Registry.BIOME_KEY, identifier);
    }
    
    public static Biome createBiome(com.dfsek.terra.api.world.biome.Biome biome, Biome vanilla) {
        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
        
        BiomeEffects.Builder effects = new BiomeEffects.Builder();
        
        Biome.Builder builder = new Builder();
        
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
            
            builder.precipitation(Objects.requireNonNullElse(vanillaBiomeProperties.getPrecipitation(), vanilla.getPrecipitation()));
            
        } else {
            effects.waterColor(vanilla.getWaterColor())
                   .waterFogColor(vanilla.getWaterFogColor())
                   .fogColor(vanilla.getFogColor())
                   .skyColor(vanilla.getSkyColor());
            vanilla.getEffects().getFoliageColor().ifPresent(effects::foliageColor);
            vanilla.getEffects().getGrassColor().ifPresent(effects::grassColor);
            
            builder.precipitation(vanilla.getPrecipitation());
        }
    
        vanilla.getLoopSound().ifPresent(effects::loopSound);
        vanilla.getAdditionsSound().ifPresent(effects::additionsSound);
        vanilla.getMoodSound().ifPresent(effects::moodSound);
        vanilla.getMusic().ifPresent(effects::music);
        vanilla.getParticleConfig().ifPresent(effects::particleConfig);
        
        return builder
                .temperature(vanilla.getTemperature())
                .downfall(vanilla.getDownfall())
                .effects(effects.build())
                .spawnSettings(vanilla.getSpawnSettings())
                .generationSettings(generationSettings.build())
                .build();
    }
    
    public static String createBiomeID(ConfigPack pack, com.dfsek.terra.api.registry.key.RegistryKey biomeID) {
        return pack.getID()
                   .toLowerCase() + "/" + biomeID.getNamespace().toLowerCase(Locale.ROOT) + "/" + biomeID.getID().toLowerCase(Locale.ROOT);
    }
}
