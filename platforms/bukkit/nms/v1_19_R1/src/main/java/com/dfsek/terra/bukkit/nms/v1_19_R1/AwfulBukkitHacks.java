package com.dfsek.terra.bukkit.nms.v1_19_R1;

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
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.dfsek.terra.api.structure.configured.ConfiguredStructure;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.bukkit.nms.v1_19_R1.config.VanillaBiomeProperties;
import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;
import com.dfsek.terra.registry.master.ConfigRegistry;


public class AwfulBukkitHacks {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwfulBukkitHacks.class);
    
    public static final Map<Holder<net.minecraft.world.level.biome.Biome>, Map<ResourceLocation,
            ProbabilityCollection<ConfiguredStructure>>>
            TERRA_BIOME_FERTILIZABLE_MAP = new HashMap<>();
    public static final Map<TagKey<Biome>, List<ResourceLocation>>
            TERRA_BIOME_TAG_MAP = new HashMap<>();
    
    public static void registerBiomes(ConfigRegistry configRegistry) {
        try {
            LOGGER.info("Hacking biome registry...");
            WritableRegistry<Biome> biomeRegistry = (WritableRegistry<Biome>) Registries.biomeRegistry();
            
            Reflection.MAPPED_REGISTRY.setFrozen((MappedRegistry<?>) biomeRegistry, false);
            
            configRegistry.forEach(pack -> pack.getRegistry(com.dfsek.terra.api.world.biome.Biome.class).forEach((key, biome) -> {
                try {
                    BukkitPlatformBiome platformBiome = (BukkitPlatformBiome) biome.getPlatformBiome();
                    
                    ResourceKey<Biome> delegateKey = ResourceKey.create(Registry.BIOME_REGISTRY,
                                                                        new ResourceLocation("terra",
                                                                                             NMSBiomeInjector.createBiomeID(pack, key)));
    
                    VanillaBiomeProperties vanillaBiomeProperties = biome.getContext().get(VanillaBiomeProperties.class);
    
                    Biome platform = NMSBiomeInjector.createBiome(vanillaBiomeProperties);
    
                    BuiltinRegistries.register(BuiltinRegistries.BIOME, delegateKey, platform);
                    biomeRegistry.register(delegateKey, platform, Lifecycle.stable());
                    platformBiome.getContext().put(new NMSBiomeInfo(delegateKey));
    
                    Map villagerMap = Reflection.VILLAGER_TYPE.getByBiome();
    
                    villagerMap.put(ResourceKey.create(Registry.BIOME_REGISTRY, delegateKey.location()),
                                    Objects.requireNonNullElse(vanillaBiomeProperties.getVillagerType(), VillagerType.PLAINS));
    
                    TERRA_BIOME_FERTILIZABLE_MAP.put(Holder.direct(platform), vanillaBiomeProperties.getFertilizables());
    
                    for(ResourceLocation tag : vanillaBiomeProperties.getTags()) {
                        TERRA_BIOME_TAG_MAP.getOrDefault(TagKey.create(Registry.BIOME_REGISTRY, tag), new ArrayList<>()).add(
                                delegateKey.location());
                    }
    
                    LOGGER.debug("Registered biome: " + delegateKey);
                } catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }));
            
            Reflection.MAPPED_REGISTRY.setFrozen((MappedRegistry<?>) biomeRegistry, true); // freeze registry again :)
    
            LOGGER.info("Doing data-driven biome tag garbage....");
            LOGGER.info("who let this data drive?");
            Map<TagKey<Biome>, List<Holder<Biome>>> collect = biomeRegistry
                    .getTags() // streamKeysAndEntries
                    .collect(HashMap::new,
                             (map, pair) ->
                                     map.put(pair.getFirst(), new ArrayList<>(pair.getSecond().stream().toList())),
                             HashMap::putAll);
    
            TERRA_BIOME_TAG_MAP.forEach((tag, biomeList) -> {
                collect.getOrDefault(tag, new ArrayList<>())
                       .addAll(biomeList.stream()
                                        .map(biomeRegistry::getOptional)
                                        .filter(Optional::isPresent)
                                        .map(Optional::get)
                                        .map(Holder::direct)
                                        .toList());
            });
            
            biomeRegistry.resetTags();
            biomeRegistry.bindTags(ImmutableMap.copyOf(collect));
            
        } catch(SecurityException | IllegalArgumentException exception) {
            throw new RuntimeException(exception);
        }
    }
}
