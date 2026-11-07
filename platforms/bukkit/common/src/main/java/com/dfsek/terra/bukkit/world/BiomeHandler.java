package com.dfsek.terra.bukkit.world;

import dev.wyck.biome.Biome;
import dev.wyck.biome.BiomeSpecialEffects;
import dev.wyck.biome.ClimateSettings;
import dev.wyck.environment.attribute.EnvironmentAttributeMap;
import dev.wyck.environment.attribute.EnvironmentAttributeSupplier;
import dev.wyck.environment.attribute.EnvironmentAttributes;
import dev.wyck.environment.sounds.AmbientSounds;
import dev.wyck.environment.sounds.BackgroundMusic;
import dev.wyck.keys.ResourceKey;
import dev.wyck.registry.BiomeRegistry;
import dev.wyck.tags.TagSet;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.tag.Tag;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.bukkit.config.VanillaBiomeProperties;
import com.dfsek.terra.registry.master.ConfigRegistry;


public final class BiomeHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(BiomeHandler.class);
    private static final Map<NamespacedKey, Villager.Type> VILLAGER_TYPES = new ConcurrentHashMap<>();

    private BiomeHandler() { }

    public static void registerBiomes(ConfigRegistry configRegistry) {
        org.bukkit.Registry<org.bukkit.block.Biome> bukkitRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME);
        List<Biome> toRegister = new ArrayList<>();
        List<Runnable> afterRegistration = new ArrayList<>();

        configRegistry.forEach(pack -> pack.getRegistry(com.dfsek.terra.api.world.biome.Biome.class).forEach((key, biome) -> {
            BukkitPlatformBiome platformBiome = (BukkitPlatformBiome) biome.getPlatformBiome();
            ResourceKey vanillaKey = ResourceKey.of(platformBiome.getHandle().getKey());
            ResourceKey terraKey = ResourceKey.of("terra", createBiomeID(pack, key));

            NamespacedKey bukkitKey = new NamespacedKey(terraKey.namespace(), terraKey.path());
            VanillaBiomeProperties properties = biome.getContext().has(VanillaBiomeProperties.class)
                                                ? biome.getContext().get(VanillaBiomeProperties.class)
                                                : new VanillaBiomeProperties();

            if(bukkitRegistry.get(bukkitKey) == null) {
                toRegister.add(createBiome(terraKey, getBiomeOrThrow(vanillaKey), properties));
            }

            afterRegistration.add(() -> {
                org.bukkit.block.Biome bukkitBiome = bukkitRegistry.getOrThrow(bukkitKey);
                platformBiome.getContext().put(new BukkitBiomeInfo(terraKey, vanillaKey, bukkitBiome));
                if(properties.getVillagerType() != null) {
                    VILLAGER_TYPES.put(bukkitKey, properties.getVillagerType());
                }
            });
        }));

        LOGGER.info("Registering {} custom biomes through Wyck...", toRegister.size());
        BiomeRegistry.registry().register(toRegister);
        afterRegistration.forEach(Runnable::run);
    }

    public static void addToVanillaTags(ConfigRegistry configRegistry) {
        Map<TypedKey<org.bukkit.block.Biome>, Set<Biome>> terraBiomesByVanilla = new HashMap<>();
        configRegistry.forEach(pack -> pack.getRegistry(com.dfsek.terra.api.world.biome.Biome.class).forEach(biome -> {
            Context context = ((BukkitPlatformBiome) biome.getPlatformBiome()).getContext();
            if(context.has(BukkitBiomeInfo.class)) {
                BukkitBiomeInfo info = context.get(BukkitBiomeInfo.class);
                TypedKey<org.bukkit.block.Biome> vanillaKey = TypedKey.create(RegistryKey.BIOME, info.vanillaKey().key());
                terraBiomesByVanilla.computeIfAbsent(vanillaKey, k -> new HashSet<>()).add(Biome.reference(info.biomeKey()));
            }
        }));

        org.bukkit.Registry<org.bukkit.block.Biome> bukkitRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME);
        int count = 0;
        for(Tag<org.bukkit.block.Biome> tag : bukkitRegistry.getTags()) {
            Set<Biome> members = new HashSet<>();
            terraBiomesByVanilla.forEach((vanillaKey, terraBiomes) -> {
                if(tag.contains(vanillaKey)) {
                    members.addAll(terraBiomes);
                }
            });
            if(!members.isEmpty()) {
                TagSet.ofBiomes(ResourceKey.of(tag.tagKey().key()), members).append();
                count++;
            }
        }
        LOGGER.info("Added Terra biomes to {} vanilla biome tags.", count);
    }

    public static @Nullable Villager.Type getVillagerType(org.bukkit.block.Biome biome) {
        return VILLAGER_TYPES.get(biome.getKey());
    }

    public static Biome getBiomeOrThrow(ResourceKey key) {
        return Biome.reference(key).wrap();
    }

    public static Biome createBiome(ResourceKey key, Biome vanilla, VanillaBiomeProperties properties) {
        EnvironmentAttributeMap attributes = vanilla.attributes();
        attributes = override(attributes, EnvironmentAttributes.FOG_COLOR, properties.getFogColor());
        attributes = override(attributes, EnvironmentAttributes.WATER_FOG_COLOR, properties.getWaterFogColor());
        attributes = override(attributes, EnvironmentAttributes.SKY_COLOR, properties.getSkyColor());
        attributes = override(attributes, EnvironmentAttributes.AMBIENT_PARTICLES, properties.getParticles());
        attributes = override(attributes, EnvironmentAttributes.MUSIC_VOLUME, properties.getMusicVolume());
        attributes = override(attributes, EnvironmentAttributes.NATURAL_MOB_SPAWNS, properties.getSpawnSettings());
        if(properties.getMusic() != null) {
            attributes = attributes.with(EnvironmentAttributes.BACKGROUND_MUSIC,
                BackgroundMusic.builder().defaultMusic(properties.getMusic()).build());
        }
        if(properties.getLoopSound() != null || properties.getMoodSound() != null || properties.getAdditionsSound() != null) {
            AmbientSounds sounds = Objects.requireNonNullElse(attributes.get(EnvironmentAttributes.AMBIENT_SOUNDS), AmbientSounds.EMPTY);
            attributes = attributes.with(EnvironmentAttributes.AMBIENT_SOUNDS, AmbientSounds.of(
                properties.getLoopSound() != null ? properties.getLoopSound() : sounds.loop().orElse(null),
                properties.getMoodSound() != null ? properties.getMoodSound() : sounds.mood().orElse(null),
                properties.getAdditionsSound() != null ? List.of(properties.getAdditionsSound()) : sounds.additions()));
        }

        ClimateSettings climate = vanilla.climateSettings();
        BiomeSpecialEffects effects = vanilla.specialEffects();

        return Biome.builder(key)
            .attributes(attributes)
            .climateSettings(ClimateSettings.builder()
                .hasPrecipitation(Objects.requireNonNullElse(properties.getPrecipitation(), climate.hasPrecipitation()))
                .temperature(Objects.requireNonNullElse(properties.getTemperature(), climate.temperature()))
                .temperatureModifier(Objects.requireNonNullElse(properties.getTemperatureModifier(), climate.temperatureModifier()))
                .downfall(Objects.requireNonNullElse(properties.getDownfall(), climate.downfall()))
                .build())
            .specialEffects(BiomeSpecialEffects.builder()
                .waterColor(Objects.requireNonNullElse(properties.getWaterColor(), effects.waterColor()))
                .grassColorModifier(Objects.requireNonNullElse(properties.getGrassColorModifier(), effects.grassColorModifier()))
                .grassColorOverride(orElse(properties.getGrassColor(), effects.grassColorOverride().orElse(null)))
                .foliageColorOverride(orElse(properties.getFoliageColor(), effects.foliageColorOverride().orElse(null)))
                .dryFoliageColorOverride(orElse(properties.getDryFoliageColor(), effects.dryFoliageColorOverride().orElse(null)))
                .build())
            .build();
    }

    public static String createBiomeID(ConfigPack pack, com.dfsek.terra.api.registry.key.RegistryKey biomeID) {
        return pack.getID().toLowerCase(Locale.ROOT)
               + "/" + biomeID.getNamespace().toLowerCase(Locale.ROOT)
               + "/" + biomeID.getID().toLowerCase(Locale.ROOT);
    }

    private static <T> EnvironmentAttributeMap override(EnvironmentAttributeMap map, EnvironmentAttributeSupplier<T> attribute,
                                                        @Nullable T value) {
        return value == null ? map : map.with(attribute, value);
    }

    private static @Nullable Integer orElse(@Nullable Integer value, @Nullable Integer fallback) {
        return value != null ? value : fallback;
    }
}
