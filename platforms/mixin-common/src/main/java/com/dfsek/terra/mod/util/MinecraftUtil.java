package com.dfsek.terra.mod.util;

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

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.entity.Container;
import com.dfsek.terra.api.block.entity.MobSpawner;
import com.dfsek.terra.api.block.entity.Sign;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.mod.config.VanillaBiomeProperties;
import com.dfsek.terra.mod.mixin.access.BiomeAccessor;
import com.dfsek.terra.mod.mixin_ifaces.FloraFeatureHolder;


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
                                                                                                 List<ConfiguredFeature<?, ?>> flowerFeatures = List.copyOf(
                                                                                                         vanilla.getGenerationSettings()
                                                                                                                .getFlowerFeatures());
                                                                                                 logger.debug("Injecting flora into biome" +
                                                                                                              " {} : {}", tb,
                                                                                                              flowerFeatures);
                                                                                                 ((FloraFeatureHolder) terra.getGenerationSettings()).setFloraFeatures(
                                                                                                         flowerFeatures);
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
    
    public static Biome createBiome(com.dfsek.terra.api.world.biome.Biome biome, Biome vanilla,
                                    VanillaBiomeProperties vanillaBiomeProperties) {
        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
        
        BiomeEffects.Builder effects = new BiomeEffects.Builder();
        
        net.minecraft.world.biome.Biome.Builder builder = new Builder();
        
        effects.waterColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterColor(), vanilla.getWaterColor()))
               .waterFogColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterFogColor(), vanilla.getWaterFogColor()))
               .fogColor(Objects.requireNonNullElse(vanillaBiomeProperties.getFogColor(), vanilla.getFogColor()))
               .skyColor(Objects.requireNonNullElse(vanillaBiomeProperties.getSkyColor(), vanilla.getSkyColor()))
               .grassColorModifier(
                       Objects.requireNonNullElse(vanillaBiomeProperties.getGrassColorModifier(),
                                                  vanilla.getEffects().getGrassColorModifier()));
        
        if(vanillaBiomeProperties.getFoliageColor() == null) {
            vanilla.getEffects().getFoliageColor().ifPresent(effects::foliageColor);
        } else {
            effects.foliageColor(vanillaBiomeProperties.getFoliageColor());
        }
        
        if(vanillaBiomeProperties.getGrassColor() == null) {
            vanilla.getEffects().getGrassColor().ifPresent(effects::grassColor);
        } else {
            effects.grassColor(vanillaBiomeProperties.getGrassColor());
        }
        
        if(vanillaBiomeProperties.getParticleConfig() == null) {
            vanilla.getEffects().getParticleConfig().ifPresent(effects::particleConfig);
        } else {
            effects.particleConfig(vanillaBiomeProperties.getParticleConfig());
        }
        
        if(vanillaBiomeProperties.getLoopSound() == null) {
            vanilla.getEffects().getLoopSound().ifPresent(effects::loopSound);
        } else {
            effects.loopSound(vanillaBiomeProperties.getLoopSound());
        }
        
        if(vanillaBiomeProperties.getMoodSound() == null) {
            vanilla.getEffects().getMoodSound().ifPresent(effects::moodSound);
        } else {
            effects.moodSound(vanillaBiomeProperties.getMoodSound());
        }
        
        if(vanillaBiomeProperties.getAdditionsSound() == null) {
            vanilla.getEffects().getAdditionsSound().ifPresent(effects::additionsSound);
        } else {
            effects.additionsSound(vanillaBiomeProperties.getAdditionsSound());
        }
        
        if(vanillaBiomeProperties.getMusic() == null) {
            vanilla.getEffects().getMusic().ifPresent(effects::music);
        } else {
            effects.music(vanillaBiomeProperties.getMusic());
        }
        
        builder.precipitation(Objects.requireNonNullElse(vanillaBiomeProperties.getPrecipitation(), vanilla.getPrecipitation()));
        
        builder.temperature(Objects.requireNonNullElse(vanillaBiomeProperties.getTemperature(), vanilla.getTemperature()));
        
        builder.downfall(Objects.requireNonNullElse(vanillaBiomeProperties.getDownfall(), vanilla.getDownfall()));
        
        builder.temperatureModifier(Objects.requireNonNullElse(vanillaBiomeProperties.getTemperatureModifier(),
                                                               ((BiomeAccessor) ((Object) vanilla)).getWeather().temperatureModifier()));
        
        builder.spawnSettings(Objects.requireNonNullElse(vanillaBiomeProperties.getSpawnSettings(), vanilla.getSpawnSettings()));
        
        return builder
                .effects(effects.build())
                .generationSettings(generationSettings.build())
                .build();
    }
    
    public static String createBiomeID(ConfigPack pack, com.dfsek.terra.api.registry.key.RegistryKey biomeID) {
        return pack.getID()
                   .toLowerCase() + "/" + biomeID.getNamespace().toLowerCase(Locale.ROOT) + "/" + biomeID.getID().toLowerCase(Locale.ROOT);
    }
}
