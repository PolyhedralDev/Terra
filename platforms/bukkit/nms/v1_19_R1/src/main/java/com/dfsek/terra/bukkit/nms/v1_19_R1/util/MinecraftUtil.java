package com.dfsek.terra.bukkit.nms.v1_19_R1.util;

import com.dfsek.terra.bukkit.nms.v1_19_R1.config.VanillaBiomeProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeBuilder;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.entity.Container;
import com.dfsek.terra.api.block.entity.MobSpawner;
import com.dfsek.terra.api.block.entity.Sign;
import com.dfsek.terra.api.config.ConfigPack;


public final class MinecraftUtil {
    public static final Logger logger = LoggerFactory.getLogger(MinecraftUtil.class);
    
    
    private MinecraftUtil() {
    
    }
    
    public static <T> Optional<Holder<T>> getEntry(Registry<T> registry, ResourceLocation identifier) {
        return registry.getOptional(identifier)
                       .flatMap(registry::getResourceKey)
                       .map(registry::getOrCreateHolderOrThrow);
    }
    
    public static BlockEntity createState(LevelAccessor worldAccess, BlockPos pos) {
        net.minecraft.world.level.block.entity.BlockEntity entity = worldAccess.getBlockEntity(pos);
        if(entity instanceof SignBlockEntity) {
            return (Sign) entity;
        } else if(entity instanceof SpawnerBlockEntity) {
            return (MobSpawner) entity;
        } else if(entity instanceof RandomizableContainerBlockEntity) {
            return (Container) entity;
        }
        return null;
    }
    
    public static ResourceKey<Biome> registerKey(ResourceLocation identifier) {
        return ResourceKey.create(Registry.BIOME_REGISTRY, identifier);
    }
    
    public static Biome createBiome(VanillaBiomeProperties vanillaBiomeProperties) {
        
        BiomeGenerationSettings.Builder generationSettings = new BiomeGenerationSettings.Builder();
        
        BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder();
        
        BiomeBuilder builder = new BiomeBuilder();
        
        effects.waterColor(Objects.requireNonNull(vanillaBiomeProperties.getWaterColor()))
               .waterFogColor(Objects.requireNonNull(vanillaBiomeProperties.getWaterFogColor()))
               .fogColor(Objects.requireNonNull(vanillaBiomeProperties.getFogColor()))
               .skyColor(Objects.requireNonNull(vanillaBiomeProperties.getSkyColor()))
               .grassColorModifier(
                       Objects.requireNonNull(vanillaBiomeProperties.getGrassColorModifier()));
        
        if(vanillaBiomeProperties.getFoliageColor() != null) {
            effects.foliageColorOverride(vanillaBiomeProperties.getFoliageColor());
        }
        
        if(vanillaBiomeProperties.getGrassColor() != null) {
            effects.grassColorOverride(vanillaBiomeProperties.getGrassColor());
        }
        
        if(vanillaBiomeProperties.getParticleConfig() != null) {
            effects.ambientParticle(vanillaBiomeProperties.getParticleConfig());
        }
        
        if(vanillaBiomeProperties.getLoopSound() != null) {
            effects.ambientLoopSound(vanillaBiomeProperties.getLoopSound());
        }
        
        if(vanillaBiomeProperties.getMoodSound() != null) {
            effects.ambientMoodSound(vanillaBiomeProperties.getMoodSound());
        }
        
        if(vanillaBiomeProperties.getAdditionsSound() != null) {
            effects.ambientAdditionsSound(vanillaBiomeProperties.getAdditionsSound());
        }
        
        if(vanillaBiomeProperties.getMusic() != null) {
            effects.backgroundMusic(vanillaBiomeProperties.getMusic());
        }
        
        builder.precipitation(Objects.requireNonNull(vanillaBiomeProperties.getPrecipitation()));
        
        builder.temperature(Objects.requireNonNull(vanillaBiomeProperties.getTemperature()));
        
        builder.downfall(Objects.requireNonNull(vanillaBiomeProperties.getDownfall()));
        
        builder.temperatureAdjustment(Objects.requireNonNull(vanillaBiomeProperties.getTemperatureModifier()));
        
        builder.mobSpawnSettings(Objects.requireNonNull(vanillaBiomeProperties.getSpawnSettings()));
    
        return builder
                .specialEffects(effects.build())
                .generationSettings(generationSettings.build())
                .build();
    }
    
    public static String createBiomeID(ConfigPack pack, com.dfsek.terra.api.registry.key.RegistryKey biomeID) {
        return pack.getID()
                   .toLowerCase() + "/" + biomeID.getNamespace().toLowerCase(Locale.ROOT) + "/" + biomeID.getID().toLowerCase(Locale.ROOT);
    }
}
