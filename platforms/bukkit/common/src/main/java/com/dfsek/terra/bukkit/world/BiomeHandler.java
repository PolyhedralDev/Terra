package com.dfsek.terra.bukkit.world;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.bukkit.config.VanillaBiomeProperties;

import dev.wyck.biome.Biome;
import dev.wyck.biome.Biome.Builder;
import dev.wyck.biome.BiomeGenerationSettings;
import dev.wyck.biome.BiomeSpecialEffects;
import dev.wyck.biome.ClimateSettings;
import dev.wyck.environment.attribute.EnvironmentAttributeSupplier;
import dev.wyck.environment.attribute.EnvironmentAttributes;
import dev.wyck.environment.sounds.AmbientSounds;
import dev.wyck.environment.sounds.BackgroundMusic;
import dev.wyck.keys.KeyChains;
import dev.wyck.keys.ResourceKey;

import java.util.Locale;
import java.util.Objects;


public class BiomeHandler {

    public static Biome getBiome(ResourceKey key) {
        Biome biome = KeyChains.BIOMES.get(key);
        return biome != null ? biome.wrap() : null;
    }

    public static Biome getBiomeOrThrow(ResourceKey key) {
        return KeyChains.BIOMES.getOrThrow(key).wrap();
    }

    public static Biome createBiome(ResourceKey vanillaBiomeKey, VanillaBiomeProperties vanillaBiomeProperties)
    throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Biome vanilla = getBiomeOrThrow(vanillaBiomeKey);
        Builder builder = dev.wyck.biome.Biome.builder();

        setAttributeOrDefault(builder, vanilla, EnvironmentAttributes.FOG_COLOR, vanillaBiomeProperties.getFogColor());
        setAttributeOrDefault(builder, vanilla, EnvironmentAttributes.WATER_FOG_COLOR, vanillaBiomeProperties.getWaterFogColor());
        setAttributeOrDefault(builder, vanilla, EnvironmentAttributes.SKY_COLOR, vanillaBiomeProperties.getSkyColor());
        setAttributeOrDefault(builder, vanilla, EnvironmentAttributes.AMBIENT_PARTICLES, vanillaBiomeProperties.getParticles());
        setAttributeOrDefault(builder, vanilla, EnvironmentAttributes.AMBIENT_SOUNDS, AmbientSounds.builder()
            .loop(vanillaBiomeProperties.getLoopSound())
            .mood(vanillaBiomeProperties.getMoodSound())
            .addition(vanillaBiomeProperties.getAdditionsSound())
            .build());
        setAttributeOrDefault(builder, vanilla, EnvironmentAttributes.BACKGROUND_MUSIC, BackgroundMusic.builder()
            .defaultMusic(vanillaBiomeProperties.getMusic())
            .build());
        setAttributeOrDefault(builder, vanilla, EnvironmentAttributes.MUSIC_VOLUME, vanillaBiomeProperties.getMusicVolume());
        setAttributeOrDefault(builder, vanilla, EnvironmentAttributes.NATURAL_MOB_SPAWNS, vanillaBiomeProperties.getSpawnSettings());

        builder.climateSettings(ClimateSettings.builder()
            .hasPrecipitation(Objects.requireNonNullElse(vanillaBiomeProperties.getPrecipitation(), vanilla.climateSettings().hasPrecipitation()))
            .temperature(Objects.requireNonNullElse(vanillaBiomeProperties.getTemperature(), vanilla.climateSettings().temperature()))
            .temperatureModifier(Objects.requireNonNullElse(vanillaBiomeProperties.getTemperatureModifier(), vanilla.climateSettings().temperatureModifier()))
            .downfall(Objects.requireNonNullElse(vanillaBiomeProperties.getDownfall(), vanilla.climateSettings().downfall()))
            .build());

        BiomeSpecialEffects.Builder effects = BiomeSpecialEffects.builder();
        effects.waterColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterColor(), vanilla.specialEffects().waterColor()));
        effects.grassColorModifier(Objects.requireNonNullElse(vanillaBiomeProperties.getGrassColorModifier(), vanilla.specialEffects().grassColorModifier()));

        if(vanillaBiomeProperties.getGrassColor() == null) {
            vanilla.specialEffects().grassColorOverride().ifPresent(effects::grassColorOverride);
        } else {
            effects.grassColorOverride(vanillaBiomeProperties.getGrassColor());
        }

        if(vanillaBiomeProperties.getFoliageColor() == null) {
            vanilla.specialEffects().foliageColorOverride().ifPresent(effects::foliageColorOverride);
        } else {
            effects.foliageColorOverride(vanillaBiomeProperties.getFoliageColor());
        }

        return builder
            .specialEffects(effects.build())
            .generationSettings(BiomeGenerationSettings.builder()
                .build())
            .build();
    }

    public static String createBiomeID(ConfigPack pack, com.dfsek.terra.api.registry.key.RegistryKey biomeID) {
        return pack.getID().toLowerCase(Locale.ROOT)
               + "/" + biomeID.getNamespace().toLowerCase(Locale.ROOT)
               + "/" + biomeID.getID().toLowerCase(Locale.ROOT);
    }

    private static <T> void setAttributeOrDefault(Biome.Builder builder, Biome def, EnvironmentAttributeSupplier<T> attribute, T value) {
        builder.attribute(attribute, Objects.requireNonNullElse(value, def.attributes().get(attribute)));
    }
}
