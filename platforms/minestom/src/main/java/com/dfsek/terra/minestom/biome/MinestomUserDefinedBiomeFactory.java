package com.dfsek.terra.minestom.biome;

import net.kyori.adventure.key.Key;
import net.minestom.server.MinecraftServer;
import net.minestom.server.color.Color;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.attribute.EnvironmentAttribute;
import net.minestom.server.world.biome.Biome;
import net.minestom.server.world.biome.BiomeEffects;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.minestom.api.BiomeFactory;
import com.dfsek.terra.minestom.config.VanillaBiomeProperties;


public class MinestomUserDefinedBiomeFactory implements BiomeFactory {
    private final DynamicRegistry<Biome> biomeRegistry = MinecraftServer.getBiomeRegistry();
    private final @NotNull Biome plainsBiome = Objects.requireNonNull(biomeRegistry.get(Key.key("minecraft:plains")));

    private static <T> T mergeNullable(T first, T second) {
        if(first == null) return second;
        return first;
    }

    private static <T> void applyAttributeIfNotNull(Biome.Builder biomeBuilder, EnvironmentAttribute<T> attribute, T value) {
        if (value == null) return;
        biomeBuilder.setAttribute(attribute, value);
    }

    @Subst("value")
    protected static String createBiomeID(ConfigPack pack, String biomeId) {
        return pack.getID().toLowerCase() + "/" + biomeId.toLowerCase(Locale.ROOT);
    }

    @Override
    public UserDefinedBiome create(ConfigPack pack, com.dfsek.terra.api.world.biome.Biome source) {
        VanillaBiomeProperties properties = source.getContext().get(VanillaBiomeProperties.class);
        RegistryKey<Biome> parentKey = ((MinestomBiome) source.getPlatformBiome()).getHandle();
        Biome parent = mergeNullable(biomeRegistry.get(parentKey), plainsBiome);
        BiomeEffects parentEffects = parent.effects();
        Key key = Key.key("terra", createBiomeID(pack, source.getID()));

        BiomeEffects.Builder effectsBuilder = BiomeEffects.builder()
            .waterColor(mergeNullable(properties.getWaterColor(), parentEffects.waterColor()))
            .foliageColor(mergeNullable(properties.getFoliageColor(), parentEffects.foliageColor()))
            .grassColor(mergeNullable(properties.getGrassColor(), parentEffects.grassColor()))
            .grassColorModifier(mergeNullable(properties.getGrassColorModifier(), parentEffects.grassColorModifier()));

        Biome.Builder targetBuilder = Biome.builder()
            .downfall(mergeNullable(properties.getDownfall(), parent.downfall()))
            .precipitation(mergeNullable(properties.getPrecipitation(), parent.hasPrecipitation()))
            .temperature(mergeNullable(properties.getTemperature(), parent.temperature()))
            .effects(effectsBuilder.build());

        applyAttributeIfNotNull(targetBuilder, EnvironmentAttribute.FOG_COLOR, properties.getFogColor());
        applyAttributeIfNotNull(targetBuilder, EnvironmentAttribute.SKY_COLOR, properties.getSkyColor());
        applyAttributeIfNotNull(targetBuilder, EnvironmentAttribute.WATER_FOG_COLOR, properties.getWaterFogColor());
        applyAttributeIfNotNull(targetBuilder, EnvironmentAttribute.AMBIENT_PARTICLES, properties.getParticleConfig());
        applyAttributeIfNotNull(targetBuilder, EnvironmentAttribute.AMBIENT_SOUNDS, properties.getAmbientSoundConfig());
        // TODO music

        Biome target = targetBuilder.build();

        RegistryKey<Biome> registryKey = MinecraftServer.getBiomeRegistry().register(key, target);
        return new UserDefinedBiome(key, registryKey, source.getID(), target);
    }
}
