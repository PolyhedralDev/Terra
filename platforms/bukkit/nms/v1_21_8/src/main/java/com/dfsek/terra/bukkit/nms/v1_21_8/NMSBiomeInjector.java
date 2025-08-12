package com.dfsek.terra.bukkit.nms.v1_21_8;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.bukkit.nms.v1_21_8.config.VanillaBiomeProperties;


public class NMSBiomeInjector {

    public static <T> Optional<Holder<T>> getEntry(Registry<T> registry, ResourceLocation identifier) {
        return registry.getOptional(identifier)
            .flatMap(registry::getResourceKey)
            .flatMap(registry::get);
    }

    public static Biome createBiome(Biome vanilla, VanillaBiomeProperties vanillaBiomeProperties)
    throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Biome.BiomeBuilder builder = new Biome.BiomeBuilder();

        BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder();

        effects.fogColor(Objects.requireNonNullElse(vanillaBiomeProperties.getFogColor(), vanilla.getFogColor()))
            .waterColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterColor(), vanilla.getWaterColor()))
            .waterFogColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterFogColor(), vanilla.getWaterFogColor()))
            .skyColor(Objects.requireNonNullElse(vanillaBiomeProperties.getSkyColor(), vanilla.getSkyColor()))
            .grassColorModifier(Objects.requireNonNullElse(vanillaBiomeProperties.getGrassColorModifier(), vanilla.getSpecialEffects().getGrassColorModifier()))
            .backgroundMusicVolume(Objects.requireNonNullElse(vanillaBiomeProperties.getMusicVolume(), vanilla.getBackgroundMusicVolume()));

        if(vanillaBiomeProperties.getGrassColor() == null) {
            vanilla.getSpecialEffects().getGrassColorOverride().ifPresent(effects::grassColorOverride);
        } else {
            effects.grassColorOverride(vanillaBiomeProperties.getGrassColor());
        }

        if(vanillaBiomeProperties.getFoliageColor() == null) {
            vanilla.getSpecialEffects().getFoliageColorOverride().ifPresent(effects::foliageColorOverride);
        } else {
            effects.foliageColorOverride(vanillaBiomeProperties.getFoliageColor());
        }

        if(vanillaBiomeProperties.getParticleConfig() == null) {
            vanilla.getSpecialEffects().getAmbientParticleSettings().ifPresent(effects::ambientParticle);
        } else {
            effects.ambientParticle(vanillaBiomeProperties.getParticleConfig());
        }

        if(vanillaBiomeProperties.getLoopSound() == null) {
            vanilla.getSpecialEffects().getAmbientLoopSoundEvent().ifPresent(effects::ambientLoopSound);
        } else {
            RegistryFetcher.soundEventRegistry().get(vanillaBiomeProperties.getLoopSound().location()).ifPresent(effects::ambientLoopSound);
        }

        if(vanillaBiomeProperties.getMoodSound() == null) {
            vanilla.getSpecialEffects().getAmbientMoodSettings().ifPresent(effects::ambientMoodSound);
        } else {
            effects.ambientMoodSound(vanillaBiomeProperties.getMoodSound());
        }

        if(vanillaBiomeProperties.getAdditionsSound() == null) {
            vanilla.getSpecialEffects().getAmbientAdditionsSettings().ifPresent(effects::ambientAdditionsSound);
        } else {
            effects.ambientAdditionsSound(vanillaBiomeProperties.getAdditionsSound());
        }

        if(vanillaBiomeProperties.getMusic() == null) {
            vanilla.getSpecialEffects().getBackgroundMusic().ifPresent(effects::backgroundMusic);
        } else {
            effects.backgroundMusic(vanillaBiomeProperties.getMusic());
        }

        builder.hasPrecipitation(Objects.requireNonNullElse(vanillaBiomeProperties.getPrecipitation(), vanilla.hasPrecipitation()));

        builder.temperature(Objects.requireNonNullElse(vanillaBiomeProperties.getTemperature(), vanilla.getBaseTemperature()));

        builder.downfall(Objects.requireNonNullElse(vanillaBiomeProperties.getDownfall(), vanilla.climateSettings.downfall()));

        builder.temperatureAdjustment(Objects.requireNonNullElse(vanillaBiomeProperties.getTemperatureModifier(), vanilla.climateSettings.temperatureModifier()));

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
}
