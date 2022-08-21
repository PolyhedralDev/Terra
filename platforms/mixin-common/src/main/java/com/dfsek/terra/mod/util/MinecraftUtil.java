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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.entity.Container;
import com.dfsek.terra.api.block.entity.MobSpawner;
import com.dfsek.terra.api.block.entity.Sign;
import com.dfsek.terra.mod.config.VanillaBiomeProperties;


public final class MinecraftUtil {
    public static final Logger logger = LoggerFactory.getLogger(MinecraftUtil.class);
    
    
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
    
    public static RegistryKey<Biome> registerKey(Identifier identifier) {
        return RegistryKey.of(Registry.BIOME_KEY, identifier);
    }
    
    public static Biome createBiome(VanillaBiomeProperties vanillaBiomeProperties) {
        
        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();
        
        BiomeEffects.Builder effects = new BiomeEffects.Builder();
        
        net.minecraft.world.biome.Biome.Builder builder = new Builder();
        
        effects.waterColor(Objects.requireNonNull(vanillaBiomeProperties.getWaterColor()))
               .waterFogColor(Objects.requireNonNull(vanillaBiomeProperties.getWaterFogColor()))
               .fogColor(Objects.requireNonNull(vanillaBiomeProperties.getFogColor()))
               .skyColor(Objects.requireNonNull(vanillaBiomeProperties.getSkyColor()))
               .grassColorModifier(
                       Objects.requireNonNull(vanillaBiomeProperties.getGrassColorModifier()));
        
        if(vanillaBiomeProperties.getFoliageColor() != null) {
            effects.foliageColor(vanillaBiomeProperties.getFoliageColor());
        }
        
        if(vanillaBiomeProperties.getGrassColor() != null) {
            effects.grassColor(vanillaBiomeProperties.getGrassColor());
        }
        
        if(vanillaBiomeProperties.getParticleConfig() != null) {
            effects.particleConfig(vanillaBiomeProperties.getParticleConfig());
        }
        
        if(vanillaBiomeProperties.getLoopSound() != null) {
            effects.loopSound(vanillaBiomeProperties.getLoopSound());
        }
        
        if(vanillaBiomeProperties.getMoodSound() != null) {
            effects.moodSound(vanillaBiomeProperties.getMoodSound());
        }
        
        if(vanillaBiomeProperties.getAdditionsSound() != null) {
            effects.additionsSound(vanillaBiomeProperties.getAdditionsSound());
        }
        
        if(vanillaBiomeProperties.getMusic() != null) {
            effects.music(vanillaBiomeProperties.getMusic());
        }
        
        builder.precipitation(Objects.requireNonNull(vanillaBiomeProperties.getPrecipitation()));
        
        builder.temperature(Objects.requireNonNull(vanillaBiomeProperties.getTemperature()));
        
        builder.downfall(Objects.requireNonNull(vanillaBiomeProperties.getDownfall()));
        
        builder.temperatureModifier(Objects.requireNonNull(vanillaBiomeProperties.getTemperatureModifier()));
        
        builder.spawnSettings(Objects.requireNonNull(vanillaBiomeProperties.getSpawnSettings()));
        
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
