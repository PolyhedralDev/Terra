package com.dfsek.terra.bukkit.nms.v1_18_R2;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.NamespacedKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;
import com.dfsek.terra.registry.master.ConfigRegistry;


public class AwfulBukkitHacks {
    private static final Logger LOGGER = LoggerFactory.getLogger(NMSBiomeInjector.class);
    private static final Map<ResourceLocation, List<ResourceLocation>> terraBiomeMap = new HashMap<>();
    
    
    public static void registerBiomes(ConfigRegistry configRegistry) {
        try {
            LOGGER.info("Hacking biome registry...");
            WritableRegistry<Biome> biomeRegistry = (WritableRegistry<Biome>) Registries.biomeRegistry();
            
            Reflection.MAPPED_REGISTRY.setFrozen((MappedRegistry<?>) biomeRegistry, false);
            
            configRegistry.forEach(pack -> pack.getRegistry(com.dfsek.terra.api.world.biome.Biome.class).forEach((key, biome) -> {
                try {
                    BukkitPlatformBiome platformBiome = (BukkitPlatformBiome) biome.getPlatformBiome();
                    NamespacedKey vanillaBukkitKey = platformBiome.getHandle().getKey();
                    ResourceLocation vanillaMinecraftKey = new ResourceLocation(vanillaBukkitKey.getNamespace(), vanillaBukkitKey.getKey());
                    Biome platform = NMSBiomeInjector.createBiome(
                            biome,
                            biomeRegistry.get(vanillaMinecraftKey) // get
                                                                 );
                    
                    ResourceKey<Biome> delegateKey = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation("terra",
                                                                                                                      NMSBiomeInjector.createBiomeID(
                                                                                                                              pack, key)));
                    
                    BuiltinRegistries.register(BuiltinRegistries.BIOME, delegateKey, platform);
                    biomeRegistry.register(delegateKey, platform, Lifecycle.stable());
                    platformBiome.getContext().put(new NMSBiomeInfo(delegateKey));
                    
                    terraBiomeMap.computeIfAbsent(vanillaMinecraftKey, i -> new ArrayList<>()).add(delegateKey.location());
                    
                    LOGGER.debug("Registered biome: " + delegateKey);
                } catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }));
            
            Reflection.MAPPED_REGISTRY.setFrozen((MappedRegistry<?>) biomeRegistry, true); // freeze registry again :)
            
            LOGGER.info("Doing tag garbage....");
            Map<TagKey<Biome>, List<Holder<Biome>>> collect = biomeRegistry
                    .getTags() // streamKeysAndEntries
                    .collect(HashMap::new,
                             (map, pair) ->
                                     map.put(pair.getFirst(), new ArrayList<>(pair.getSecond().stream().toList())),
                             HashMap::putAll);
            
            terraBiomeMap
                    .forEach((vb, terraBiomes) ->
                                     NMSBiomeInjector.getEntry(biomeRegistry, vb)
                                                     .ifPresentOrElse(
                                                             vanilla -> terraBiomes
                                                                     .forEach(tb -> NMSBiomeInjector.getEntry(biomeRegistry, tb)
                                                                                                    .ifPresentOrElse(
                                                                                                            terra -> {
                                                                                                                LOGGER.debug(
                                                                                                                        vanilla.unwrapKey()
                                                                                                                               .orElseThrow()
                                                                                                                               .location() +
                                                                                                                        " (vanilla for " +
                                                                                                                        terra.unwrapKey()
                                                                                                                             .orElseThrow()
                                                                                                                             .location() +
                                                                                                                        ": " +
                                                                                                                        vanilla.tags()
                                                                                                                               .toList());

                                                                                                                vanilla.tags()
                                                                                                                       .forEach(
                                                                                                                               tag -> collect
                                                                                                                                       .computeIfAbsent(
                                                                                                                                               tag,
                                                                                                                                               t -> new ArrayList<>())
                                                                                                                                       .add(terra));
                                                                                                            },
                                                                                                            () -> LOGGER.error(
                                                                                                                    "No such biome: {}",
                                                                                                                    tb))),
                                                             () -> LOGGER.error("No vanilla biome: {}", vb)));
            
            biomeRegistry.resetTags(); // clearTags
            biomeRegistry.bindTags(ImmutableMap.copyOf(collect)); // populateTags
            
        } catch(SecurityException | IllegalArgumentException exception) {
            throw new RuntimeException(exception);
        }
    }
}
