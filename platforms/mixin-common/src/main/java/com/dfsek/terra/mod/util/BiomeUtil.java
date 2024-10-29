package com.dfsek.terra.mod.util;

import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Builder;
import net.minecraft.world.biome.BiomeEffects;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.mod.config.VanillaBiomeProperties;
import com.dfsek.terra.mod.mixin.access.BiomeAccessor;
import com.dfsek.terra.mod.mixin.invoke.BiomeInvoker;


public class BiomeUtil {
    public static final Map<Identifier, List<Identifier>>
        TERRA_BIOME_MAP = new HashMap<>();

    public static Biome createBiome(Biome vanilla, VanillaBiomeProperties vanillaBiomeProperties) {
        BiomeEffects.Builder effects = new BiomeEffects.Builder();

        net.minecraft.world.biome.Biome.Builder builder = new Builder();

        effects.waterColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterColor(), vanilla.getWaterColor()))
            .waterFogColor(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterFogColor(), vanilla.getWaterFogColor()))
            .fogColor(Objects.requireNonNullElse(vanillaBiomeProperties.getFogColor(), vanilla.getFogColor()))
            .skyColor(Objects.requireNonNullElse(vanillaBiomeProperties.getSkyColor(), vanilla.getSkyColor()))
            .grassColorModifier(
                Objects.requireNonNullElse(vanillaBiomeProperties.getGrassColorModifier(), vanilla.getEffects().getGrassColorModifier()))
            .grassColor(Objects.requireNonNullElse(vanillaBiomeProperties.getGrassColor(),
                vanilla.getEffects().getGrassColor().orElseGet(() -> ((BiomeInvoker) ((Object) vanilla)).invokeGetDefaultGrassColor())))
            .foliageColor(Objects.requireNonNullElse(vanillaBiomeProperties.getFoliageColor(), vanilla.getFoliageColor()));

        if(vanillaBiomeProperties.getParticleConfig() == null) {
            vanilla.getEffects().getParticleConfig().ifPresent(effects::particleConfig);
        } else {
            effects.particleConfig(vanillaBiomeProperties.getParticleConfig());
        }

        if(vanillaBiomeProperties.getLoopSound() == null) {
            vanilla.getEffects().getLoopSound().ifPresent(effects::loopSound);
        } else {
            effects.loopSound(Registries.SOUND_EVENT.getEntry(vanillaBiomeProperties.getLoopSound()));
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

        builder.precipitation(Objects.requireNonNullElse(vanillaBiomeProperties.getPrecipitation(), vanilla.hasPrecipitation()));

        builder.temperature(Objects.requireNonNullElse(vanillaBiomeProperties.getTemperature(), vanilla.getTemperature()));

        builder.downfall(Objects.requireNonNullElse(vanillaBiomeProperties.getDownfall(),
            ((BiomeAccessor) ((Object) vanilla)).getWeather().downfall()));

        builder.temperatureModifier(Objects.requireNonNullElse(vanillaBiomeProperties.getTemperatureModifier(),
            ((BiomeAccessor) ((Object) vanilla)).getWeather().temperatureModifier()));

        builder.spawnSettings(Objects.requireNonNullElse(vanillaBiomeProperties.getSpawnSettings(), vanilla.getSpawnSettings()));

        return builder
            .effects(effects.build())
            .generationSettings(vanilla.getGenerationSettings())
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
