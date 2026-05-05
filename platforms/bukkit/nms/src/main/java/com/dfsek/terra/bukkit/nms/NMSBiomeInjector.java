package com.dfsek.terra.bukkit.nms;

import com.dfsek.terra.api.registry.key.RegistryKey;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.attribute.AmbientAdditionsSettings;
import net.minecraft.world.attribute.AmbientMoodSettings;
import net.minecraft.world.attribute.AmbientParticle;
import net.minecraft.world.attribute.AmbientSounds;
import net.minecraft.world.attribute.BackgroundMusic;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributeMap.Entry;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.modifier.AttributeModifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.BiomeBuilder;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.bukkit.nms.config.VanillaBiomeProperties;


public class NMSBiomeInjector {

    public static <T> Optional<Holder<T>> getEntry(Registry<T> registry, Identifier identifier) {
        return registry.getOptional(identifier).flatMap(registry::getResourceKey).flatMap(registry::get);
    }

    public static Biome createBiome(Biome vanilla, VanillaBiomeProperties vanillaBiomeProperties)
    throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Biome.BiomeBuilder builder = new Biome.BiomeBuilder();

        BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder();
        EnvironmentAttributeMap attributes = vanilla.getAttributes();

        Integer vanillaFogColour = extractInt(attributes, EnvironmentAttributes.FOG_COLOR);
        Integer vanillaWaterFogColour = extractInt(attributes, EnvironmentAttributes.WATER_FOG_COLOR);
        Integer vanillaSkyColour = extractInt(attributes, EnvironmentAttributes.SKY_COLOR);
        Float vanillaMusicVolume = extractFloat(attributes, EnvironmentAttributes.MUSIC_VOLUME);

        applyIfPresent(builder, EnvironmentAttributes.FOG_COLOR,
            vanillaBiomeProperties.getFogColor(), vanillaFogColour);

        applyIfPresent(builder, EnvironmentAttributes.WATER_FOG_COLOR,
            vanillaBiomeProperties.getWaterFogColor(), vanillaWaterFogColour);

        applyIfPresent(builder, EnvironmentAttributes.SKY_COLOR,
            vanillaBiomeProperties.getSkyColor(), vanillaSkyColour);

        applyIfPresent(builder, EnvironmentAttributes.MUSIC_VOLUME,
            vanillaBiomeProperties.getMusicVolume(), vanillaMusicVolume);

        effects.waterColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterColor(), vanilla.getWaterColor()));
        effects.grassColorModifier(
            Objects.requireNonNullElse(vanillaBiomeProperties.getGrassColorModifier(), vanilla.getSpecialEffects().grassColorModifier()));

        if(vanillaBiomeProperties.getGrassColor() == null) {
            vanilla.getSpecialEffects().grassColorOverride().ifPresent(effects::grassColorOverride);
        } else {
            effects.grassColorOverride(vanillaBiomeProperties.getGrassColor());
        }

        if(vanillaBiomeProperties.getFoliageColor() == null) {
            vanilla.getSpecialEffects().foliageColorOverride().ifPresent(effects::foliageColorOverride);
        } else {
            effects.foliageColorOverride(vanillaBiomeProperties.getFoliageColor());
        }

        if(vanillaBiomeProperties.getParticleConfig() == null) {
            Entry<?, ?> ambientEntry = attributes.get(EnvironmentAttributes.AMBIENT_PARTICLES);
            if(ambientEntry != null) {
                Object arg = ambientEntry.argument();

                // this is not nice
                if(arg instanceof List<?> rawList) {
                    List<AmbientParticle> ambientParticles =
                        rawList.stream()
                            .filter(AmbientParticle.class::isInstance)
                            .map(AmbientParticle.class::cast)
                            .toList();

                    builder.modifyAttribute(
                        EnvironmentAttributes.AMBIENT_PARTICLES,
                        AttributeModifier.override(),
                        ambientParticles
                    );
                }
            }
        } else {
            builder.modifyAttribute(EnvironmentAttributes.AMBIENT_PARTICLES, AttributeModifier.override(),
                List.of(vanillaBiomeProperties.getParticleConfig()));
        }

        Optional<Holder<SoundEvent>> loop = Optional.empty();
        Optional<AmbientMoodSettings> mood = Optional.empty();
        List<AmbientAdditionsSettings> additions = Collections.emptyList();

        if(attributes.contains(EnvironmentAttributes.AMBIENT_SOUNDS)) {
            AmbientSounds sounds =
                (AmbientSounds) Objects.requireNonNull(attributes.get(EnvironmentAttributes.AMBIENT_SOUNDS)).argument();

            loop = sounds.loop();
            mood = sounds.mood();
            additions = sounds.additions();
        }

        if(vanillaBiomeProperties.getLoopSound() != null) {
            loop = RegistryFetcher.soundEventRegistry()
                .get(vanillaBiomeProperties.getLoopSound().location())
                .map(ref -> ref);
        }

        if(vanillaBiomeProperties.getMoodSound() != null) {
            mood = Optional.ofNullable(vanillaBiomeProperties.getMoodSound());
        }

        if(vanillaBiomeProperties.getAdditionsSound() != null) {
            additions = Collections.singletonList(vanillaBiomeProperties.getAdditionsSound());
        }

        builder.modifyAttribute(EnvironmentAttributes.AMBIENT_SOUNDS, AttributeModifier.override(),
            new AmbientSounds(loop, mood, additions));

        if(vanillaBiomeProperties.getMusic() == null) {
            if(attributes.contains(EnvironmentAttributes.BACKGROUND_MUSIC)) {
                BackgroundMusic music = (BackgroundMusic) Objects.requireNonNull(attributes.get(EnvironmentAttributes.BACKGROUND_MUSIC))
                    .argument();
                builder.modifyAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, AttributeModifier.override(), music);
            }
        } else {
            builder.modifyAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, AttributeModifier.override(),
                new BackgroundMusic(vanillaBiomeProperties.getMusic()));
        }

        builder.hasPrecipitation(Objects.requireNonNullElse(vanillaBiomeProperties.getPrecipitation(), vanilla.hasPrecipitation()));
        builder.temperature(Objects.requireNonNullElse(vanillaBiomeProperties.getTemperature(), vanilla.getBaseTemperature()));
        builder.downfall(Objects.requireNonNullElse(vanillaBiomeProperties.getDownfall(), vanilla.climateSettings.downfall()));
        builder.temperatureAdjustment(
            Objects.requireNonNullElse(vanillaBiomeProperties.getTemperatureModifier(), vanilla.climateSettings.temperatureModifier()));
        builder.mobSpawnSettings(Objects.requireNonNullElse(vanillaBiomeProperties.getSpawnSettings(), vanilla.getMobSettings()));

        return builder.specialEffects(effects.build()).generationSettings(new BiomeGenerationSettings.PlainBuilder().build()).build();
    }

    public static String createBiomeID(ConfigPack pack, RegistryKey biomeID) {
        return pack.getID().toLowerCase() + "/" + biomeID.getNamespace().toLowerCase(Locale.ROOT) + "/" + biomeID.getID().toLowerCase(
            Locale.ROOT);
    }

    private static Integer extractInt(EnvironmentAttributeMap attributes,
                                      EnvironmentAttribute<Integer> key) {
        Entry<Integer, ?> attr = attributes.get(key);
        return attr != null ? (Integer) attr.argument() : null;
    }

    private static Float extractFloat(EnvironmentAttributeMap attributes,
                                      EnvironmentAttribute<Float> key) {
        Entry<Float, ?> attr = attributes.get(key);
        return attr != null ? (Float) attr.argument() : null;
    }

    private static <T> void applyIfPresent(
        BiomeBuilder builder,
        EnvironmentAttribute<T> attr,
        T overrideValue,
        T fallbackValue
    ) {
        T value = overrideValue != null ? overrideValue : fallbackValue;

        if (value == null) {
            return;
        }

        builder.modifyAttribute(attr, AttributeModifier.override(), value);
    }
}
