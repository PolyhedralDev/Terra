package com.dfsek.terra.minestom.biome;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.minestom.api.BiomeFactory;
import com.dfsek.terra.minestom.config.VanillaBiomeProperties;

import net.kyori.adventure.key.Key;
import net.minestom.server.MinecraftServer;
import net.minestom.server.color.Color;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.biome.Biome;
import net.minestom.server.world.biome.BiomeEffects;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;


public class MinestomUserDefinedBiomeFactory implements BiomeFactory {
    private final DynamicRegistry<Biome> biomeRegistry = MinecraftServer.getBiomeRegistry();
    private final @NotNull Biome plainsBiome = Objects.requireNonNull(biomeRegistry.get(Key.key("minecraft:plains")));

    @Override
    public UserDefinedBiome create(ConfigPack pack, com.dfsek.terra.api.world.biome.Biome source) {
        VanillaBiomeProperties properties = source.getContext().get(VanillaBiomeProperties.class);
        RegistryKey<Biome> parentKey = ((MinestomBiome) source.getPlatformBiome()).getHandle();
        Biome parent = mergeNullable(biomeRegistry.get(parentKey), plainsBiome);
        BiomeEffects parentEffects = parent.effects();
        Key key = Key.key("terra", createBiomeID(pack, source.getID()));

        BiomeEffects.Builder effectsBuilder = BiomeEffects.builder()
            .fogColor(mergeNullable(properties.getFogColor(), parentEffects.fogColor()))
            .skyColor(mergeNullable(properties.getSkyColor(), parentEffects.skyColor()))
            .waterColor(mergeNullable(properties.getWaterColor(), parentEffects.waterColor()))
            .waterFogColor(mergeNullable(properties.getWaterFogColor(), parentEffects.waterFogColor()))
            .foliageColor(mergeNullable(properties.getFoliageColor(), parentEffects.foliageColor()))
            .grassColor(mergeNullable(properties.getGrassColor(), parentEffects.grassColor()))
            .grassColorModifier(mergeNullable(properties.getGrassColorModifier(), parentEffects.grassColorModifier()))
            .biomeParticle(mergeNullable(properties.getParticleConfig(), parentEffects.biomeParticle()))
            .ambientSound(mergeNullable(properties.getLoopSound(), parentEffects.ambientSound()))
            .moodSound(mergeNullable(properties.getMoodSound(), parentEffects.moodSound()))
            .additionsSound(mergeNullable(properties.getAdditionsSound(), parentEffects.additionsSound()))
            // TODO music
            .music(parentEffects.music())
            .musicVolume(parentEffects.musicVolume());

        if (effectsBuilder.build().equals(BiomeEffects.PLAINS_EFFECTS)) {
            effectsBuilder.fogColor(new Color(0xC0D8FE)); // circumvent a minestom bug
        }

        Biome target = Biome.builder()
            .downfall(mergeNullable(properties.getDownfall(), parent.downfall()))
            .hasPrecipitation(mergeNullable(properties.getPrecipitation(), parent.hasPrecipitation()))
            .temperature(mergeNullable(properties.getTemperature(), parent.temperature()))
            .temperatureModifier(mergeNullable(properties.getTemperatureModifier(), parent.temperatureModifier()))
            .effects(effectsBuilder.build())
            .build();

        RegistryKey<Biome> registryKey = MinecraftServer.getBiomeRegistry().register(key, target);
        return new UserDefinedBiome(key, registryKey, source.getID(), target);
    }

    private static <T> T mergeNullable(T first, T second) {
        if (first == null) return second;
        return first;
    }

    @Subst("value")
    protected static String createBiomeID(ConfigPack pack, String biomeId) {
        return pack.getID().toLowerCase() + "/" + biomeId.toLowerCase(Locale.ROOT);
    }
}
