package com.dfsek.terra.mod.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeBuilder;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.mod.config.VanillaBiomeProperties;
import com.dfsek.terra.mod.mixin.access.BiomeAccessor;


public class BiomeUtil {
    public static final Map<Identifier, List<Identifier>>
        TERRA_BIOME_MAP = new HashMap<>();

    public static Biome createBiome(Biome vanilla, VanillaBiomeProperties vanillaBiomeProperties) {
        BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder();

        net.minecraft.world.level.biome.Biome.BiomeBuilder builder = new BiomeBuilder();

        effects.waterColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterColor(), vanilla.getWaterColor()))
            .waterFogColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterFogColor(), vanilla.getWaterFogColor()))
            .fogColor(Objects.requireNonNullElse(vanillaBiomeProperties.getFogColor(), vanilla.getFogColor()))
            .skyColor(Objects.requireNonNullElse(vanillaBiomeProperties.getSkyColor(), vanilla.getSkyColor()))
            .grassColorModifier(
                Objects.requireNonNullElse(vanillaBiomeProperties.getGrassColorModifier(), vanilla.getSpecialEffects().getGrassColorModifier()))
            .musicVolume(Objects.requireNonNullElse(vanillaBiomeProperties.getMusicVolume(), vanilla.getMusicVolume()));

        if(vanillaBiomeProperties.getGrassColor() == null) {
            vanilla.getSpecialEffects().getGrassColor().ifPresent(effects::grassColorOverride);
        } else {
            effects.grassColorOverride(vanillaBiomeProperties.getGrassColor());
        }

        if(vanillaBiomeProperties.getFoliageColor() == null) {
            vanilla.getSpecialEffects().getFoliageColor().ifPresent(effects::foliageColorOverride);
        } else {
            effects.foliageColorOverride(vanillaBiomeProperties.getFoliageColor());
        }

        if(vanillaBiomeProperties.getDryFoliageColor() == null) {
            vanilla.getSpecialEffects().getDryFoliageColor().ifPresent(effects::dryFoliageColorOverride);
        } else {
            effects.dryFoliageColorOverride(vanillaBiomeProperties.getDryFoliageColor());
        }

        if(vanillaBiomeProperties.getParticleConfig() == null) {
            vanilla.getSpecialEffects().getParticleConfig().ifPresent(effects::particleConfig);
        } else {
            effects.particleConfig(vanillaBiomeProperties.getParticleConfig());
        }

        if(vanillaBiomeProperties.getLoopSound() == null) {
            vanilla.getSpecialEffects().getLoopSound().ifPresent(effects::loopSound);
        } else {
            effects.loopSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(vanillaBiomeProperties.getLoopSound()));
        }

        if(vanillaBiomeProperties.getMoodSound() == null) {
            vanilla.getSpecialEffects().getMoodSound().ifPresent(effects::moodSound);
        } else {
            effects.moodSound(vanillaBiomeProperties.getMoodSound());
        }

        if(vanillaBiomeProperties.getAdditionsSound() == null) {
            vanilla.getSpecialEffects().getAdditionsSound().ifPresent(effects::additionsSound);
        } else {
            effects.additionsSound(vanillaBiomeProperties.getAdditionsSound());
        }

        if(vanillaBiomeProperties.getMusic() == null) {
            vanilla.getSpecialEffects().getMusic().ifPresent(effects::music);
        } else {
            effects.music(vanillaBiomeProperties.getMusic());
        }

        builder.hasPrecipitation(Objects.requireNonNullElse(vanillaBiomeProperties.getPrecipitation(), vanilla.hasPrecipitation()));

        builder.temperature(Objects.requireNonNullElse(vanillaBiomeProperties.getTemperature(), vanilla.getBaseTemperature()));

        builder.downfall(Objects.requireNonNullElse(vanillaBiomeProperties.getDownfall(),
            ((BiomeAccessor) ((Object) vanilla)).getWeather().downfall()));

        builder.temperatureAdjustment(Objects.requireNonNullElse(vanillaBiomeProperties.getTemperatureModifier(),
            ((BiomeAccessor) ((Object) vanilla)).getWeather().temperatureModifier()));

        builder.mobSpawnSettings(Objects.requireNonNullElse(vanillaBiomeProperties.getSpawnSettings(), vanilla.getMobSettings()));

        return builder
            .specialEffects(effects.build())
            .generationSettings(new BiomeGenerationSettings.PlainBuilder().build())
            .build();
    }

    public static String createBiomeID(ConfigPack pack, com.dfsek.terra.api.registry.key.RegistryKey biomeID) {
        return pack.getID()
                   .toLowerCase() + "/" + biomeID.getNamespace().toLowerCase(Locale.ROOT) + "/" + biomeID.getID().toLowerCase(Locale.ROOT);
    }

    public static Map<Identifier, List<Identifier>> getTerraBiomeMap() {
        return Map.copyOf(TERRA_BIOME_MAP);
    }
}
