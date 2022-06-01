package com.dfsek.terra.bukkit.nms.v1_18_R2;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryWritable;
import net.minecraft.core.RegistryMaterials;
import net.minecraft.data.RegistryGeneration;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeFog;
import net.minecraft.world.level.biome.BiomeFog.GrassColor;
import net.minecraft.world.level.biome.BiomeSettingsGeneration;
import net.minecraft.world.level.biome.BiomeSettingsMobs;
import org.bukkit.NamespacedKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.bukkit.config.VanillaBiomeProperties;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;
import com.dfsek.terra.registry.master.ConfigRegistry;


public class NMSBiomeInjector {
    private static final Logger LOGGER = LoggerFactory.getLogger(NMSBiomeInjector.class);
    private static final Map<MinecraftKey, List<MinecraftKey>> terraBiomeMap = new HashMap<>();
    
    
    public static void registerBiomes(ConfigRegistry configRegistry) {
        try {
            LOGGER.info("Hacking biome registry...");
            IRegistryWritable<BiomeBase> biomeRegistry = (IRegistryWritable<BiomeBase>) Registries.biomeRegistry();
            Field frozen = RegistryMaterials.class.getDeclaredField("bL"); // registry frozen field
            frozen.setAccessible(true);
            frozen.set(biomeRegistry, false);
            
            configRegistry.forEach(pack -> pack.getRegistry(Biome.class).forEach((key, biome) -> {
                try {
                    BukkitPlatformBiome platformBiome = (BukkitPlatformBiome) biome.getPlatformBiome();
                    NamespacedKey vanillaBukkitKey = platformBiome.getHandle().getKey();
                    MinecraftKey vanillaMinecraftKey = new MinecraftKey(vanillaBukkitKey.getNamespace(), vanillaBukkitKey.getKey());
                    BiomeBase platform = createBiome(
                            biome,
                            biomeRegistry.a(vanillaMinecraftKey) // get
                                                    );
                    
                    ResourceKey<BiomeBase> delegateKey = ResourceKey.a(IRegistry.aP, new MinecraftKey("terra", createBiomeID(pack, key)));
                    
                    RegistryGeneration.a(RegistryGeneration.i, delegateKey, platform);
                    biomeRegistry.a(delegateKey, platform, Lifecycle.stable());
                    platformBiome.getContext().put(new NMSBiomeInfo(delegateKey));
                    
                    terraBiomeMap.computeIfAbsent(vanillaMinecraftKey, i -> new ArrayList<>()).add(delegateKey.a());
                    
                    LOGGER.debug("Registered biome: " + delegateKey);
                } catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }));
            
            frozen.set(biomeRegistry, true); // freeze registry again :)
            
            LOGGER.info("Doing tag garbage....");
            Map<TagKey<BiomeBase>, List<Holder<BiomeBase>>> collect = biomeRegistry
                    .g() // streamKeysAndEntries
                    .collect(HashMap::new,
                             (map, pair) ->
                                     map.put(pair.getFirst(), new ArrayList<>(pair.getSecond().a().toList())),
                             HashMap::putAll);
    
            terraBiomeMap
                    .forEach((vb, terraBiomes) ->
                                     getEntry(biomeRegistry, vb)
                                             .ifPresentOrElse(
                                                     vanilla -> terraBiomes
                                                             .forEach(tb -> getEntry(biomeRegistry, tb)
                                                                     .ifPresentOrElse(
                                                                             terra -> {
                                                                                 LOGGER.debug(vanilla.e().orElseThrow().a() +
                                                                                              " (vanilla for " +
                                                                                              terra.e().orElseThrow().a() +
                                                                                              ": " +
                                                                                              vanilla.c().toList());
                                                                
                                                                                 vanilla.c()
                                                                                        .forEach(
                                                                                                tag -> collect
                                                                                                        .computeIfAbsent(tag,
                                                                                                                         t -> new ArrayList<>())
                                                                                                        .add(terra));
                                                                             },
                                                                             () -> LOGGER.error(
                                                                                     "No such biome: {}",
                                                                                     tb))),
                                                     () -> LOGGER.error("No vanilla biome: {}", vb)));
    
            biomeRegistry.k(); // clearTags
            biomeRegistry.a(ImmutableMap.copyOf(collect)); // populateTags
            
        } catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    public static <T> Optional<Holder<T>> getEntry(IRegistry<T> registry, MinecraftKey identifier) {
        return registry.b(identifier)
                       .flatMap(registry::c)
                       .map(registry::c);
    }
    
    private static BiomeBase createBiome(Biome biome, BiomeBase vanilla)
    throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        BiomeBase.a builder = new BiomeBase.a(); // Builder
        
        Field f = BiomeBase.class.getDeclaredField("l"); // category
        f.setAccessible(true);
        builder.a((BiomeBase.Geography) f.get(vanilla))
               .a(vanilla.c()); // getPrecipitation
        
        
        Field biomeSettingMobsField = BiomeBase.class.getDeclaredField("k"); // spawn settings
        biomeSettingMobsField.setAccessible(true);
        BiomeSettingsMobs biomeSettingMobs = (BiomeSettingsMobs) biomeSettingMobsField.get(vanilla);
        builder.a(biomeSettingMobs);
        
        
        BiomeSettingsGeneration.a generationBuilder = new BiomeSettingsGeneration.a(); // builder
        builder.a(generationBuilder.a())
               .a(vanilla.c())
               .b(vanilla.h()) // precipitation
               .a(vanilla.i()); // temp
        
        
        BiomeFog.a effects = new BiomeFog.a(); // Builder
        effects.a(GrassColor.a); // magic
        
        VanillaBiomeProperties vanillaBiomeProperties = biome.getContext().get(VanillaBiomeProperties.class);
        
        // fog
        effects.a(Objects.requireNonNullElse(vanillaBiomeProperties.getFogColor(), vanilla.f()));
        
        // water
        effects.b(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterColor(), vanilla.k()));
        
        // water fog
        effects.c(Objects.requireNonNullElse(vanillaBiomeProperties.getWaterFogColor(), vanilla.l()));
        
        // sky
        effects.d(Objects.requireNonNullElse(vanillaBiomeProperties.getSkyColor(), vanilla.a()));
        
        if(vanillaBiomeProperties.getFoliageColor() == null) {
            vanilla.j().e().ifPresent(effects::e);
        } else {
            // foliage
            effects.e(vanillaBiomeProperties.getFoliageColor());
        }
        
        if(vanillaBiomeProperties.getGrassColor() == null) {
            vanilla.j().f().ifPresent(effects::f);
        } else {
            // grass
            effects.f(vanillaBiomeProperties.getGrassColor());
        }
        
        builder.a(effects.a()); // build()
        
        return builder.a(); // build()
    }
    
    public static String createBiomeID(ConfigPack pack, com.dfsek.terra.api.registry.key.RegistryKey biomeID) {
        return pack.getID()
                   .toLowerCase() + "/" + biomeID.getNamespace().toLowerCase(Locale.ROOT) + "/" + biomeID.getID().toLowerCase(Locale.ROOT);
    }
}
