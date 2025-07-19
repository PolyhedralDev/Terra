package com.dfsek.terra.bukkit.nms.v1_21_8;

import com.dfsek.terra.bukkit.nms.v1_21_8.config.VanillaBiomeProperties;

import com.dfsek.terra.bukkit.world.BukkitBiomeInfo;

import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderSet;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.biome.Biome;
import org.bukkit.NamespacedKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.dfsek.terra.bukkit.world.BukkitPlatformBiome;
import com.dfsek.terra.registry.master.ConfigRegistry;


public class AwfulBukkitHacks {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwfulBukkitHacks.class);

    private static final Map<ResourceLocation, List<ResourceLocation>> terraBiomeMap = new HashMap<>();

    public static void registerBiomes(ConfigRegistry configRegistry) {
        try {
            LOGGER.info("Hacking biome registry...");
            MappedRegistry<Biome> biomeRegistry = (MappedRegistry<Biome>) RegistryFetcher.biomeRegistry();

            // Unfreeze the biome registry to allow modification
            Reflection.MAPPED_REGISTRY.setFrozen(biomeRegistry, false);

            // Register the terra biomes to the registry
            configRegistry.forEach(pack -> pack.getRegistry(com.dfsek.terra.api.world.biome.Biome.class).forEach((key, biome) -> {
                try {
                    BukkitPlatformBiome platformBiome = (BukkitPlatformBiome) biome.getPlatformBiome();

                    NamespacedKey vanillaBukkitKey = platformBiome.getHandle().getKey();
                    ResourceLocation vanillaMinecraftKey = ResourceLocation.fromNamespaceAndPath(vanillaBukkitKey.getNamespace(),
                        vanillaBukkitKey.getKey());

                    VanillaBiomeProperties vanillaBiomeProperties = biome.getContext().get(VanillaBiomeProperties.class);

                    Biome platform = NMSBiomeInjector.createBiome(biomeRegistry.get(vanillaMinecraftKey).orElseThrow().value(), vanillaBiomeProperties);

                    ResourceLocation delegateMinecraftKey = ResourceLocation.fromNamespaceAndPath("terra", NMSBiomeInjector.createBiomeID(pack, key));
                    NamespacedKey delegateBukkitKey = NamespacedKey.fromString(delegateMinecraftKey.toString());
                    ResourceKey<Biome> delegateKey = ResourceKey.create(Registries.BIOME, delegateMinecraftKey);

                    Reference<Biome> holder = biomeRegistry.register(delegateKey, platform, RegistrationInfo.BUILT_IN);
                    Reflection.REFERENCE.invokeBindValue(holder, platform); // IMPORTANT: bind holder.

                    platformBiome.getContext().put(new BukkitBiomeInfo(delegateBukkitKey));
                    platformBiome.getContext().put(new NMSBiomeInfo(delegateKey));

                    Map<ResourceKey<Biome>, ResourceKey<VillagerType>> villagerMap = Reflection.VILLAGER_TYPE.getByBiome();

                    villagerMap.put(delegateKey,
                        Objects.requireNonNullElse(vanillaBiomeProperties.getVillagerType(),
                            villagerMap.getOrDefault(delegateKey, VillagerType.PLAINS)));

                    terraBiomeMap.computeIfAbsent(vanillaMinecraftKey, i -> new ArrayList<>()).add(delegateKey.location());

                    LOGGER.debug("Registered biome: " + delegateKey);
                } catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }));

            Reflection.MAPPED_REGISTRY.setFrozen(biomeRegistry, true); // freeze registry again :)

            LOGGER.info("Doing tag garbage....");
            Map<TagKey<Biome>, List<Holder<Biome>>> collect = biomeRegistry
                .getTags() // streamKeysAndEntries
                .collect(HashMap::new,
                    (map, pair) ->
                        map.put(pair.key(), new ArrayList<>(Reflection.HOLDER_SET.invokeContents(pair).stream().toList())),
                    HashMap::putAll);

            terraBiomeMap
                .forEach((vb, terraBiomes) ->
                    NMSBiomeInjector.getEntry(biomeRegistry, vb).ifPresentOrElse(
                        vanilla -> terraBiomes.forEach(
                            tb -> NMSBiomeInjector.getEntry(biomeRegistry, tb).ifPresentOrElse(
                                terra -> {
                                    LOGGER.debug("{} (vanilla for {}): {}",
                                        vanilla.unwrapKey().orElseThrow().location(),
                                        terra.unwrapKey().orElseThrow().location(),
                                        vanilla.tags().toList());
                                    vanilla.tags()
                                        .forEach(tag -> collect
                                            .computeIfAbsent(tag, t -> new ArrayList<>())
                                            .add(terra));
                                },
                                () -> LOGGER.error("No such biome: {}", tb))),
                        () -> LOGGER.error("No vanilla biome: {}", vb)));

            resetTags(biomeRegistry);
            bindTags(biomeRegistry, collect);

        } catch(SecurityException | IllegalArgumentException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static <T> void bindTags(MappedRegistry<T> registry, Map<TagKey<T>, List<Holder<T>>> tagEntries) {
        Map<Holder.Reference<T>, List<TagKey<T>>> map = new IdentityHashMap<>();
        Reflection.MAPPED_REGISTRY.getByKey(registry).values().forEach(entry -> map.put(entry, new ArrayList<>()));
        tagEntries.forEach((tag, entries) -> {
            for (Holder<T> holder : entries) {
//                if (!holder.canSerializeIn(registry.asLookup())) {
//                    throw new IllegalStateException("Can't create named set " + tag + " containing value " + holder + " from outside registry " + this);
//                }

                if (!(holder instanceof Holder.Reference<T> reference)) {
                    throw new IllegalStateException("Found direct holder " + holder + " value in tag " + tag);
                }

                map.get(reference).add(tag);
            }
        });
//        Set<TagKey<T>> set = Sets.difference(registry.tags.keySet(), tagEntries.keySet());
//        if (!set.isEmpty()) {
//            LOGGER.warn(
//                "Not all defined tags for registry {} are present in data pack: {}",
//                registry.key(),
//                set.stream().map(tag -> tag.location().toString()).sorted().collect(Collectors.joining(", "))
//            );
//        }

        Map<TagKey<T>, HolderSet.Named<T>> map2 = new IdentityHashMap<>(registry.getTags().collect(Collectors.toMap(
            Named::key,
            (named) -> named
        )));
        tagEntries.forEach((tag, entries) -> Reflection.HOLDER_SET.invokeBind(map2.computeIfAbsent(tag, key -> Reflection.MAPPED_REGISTRY.invokeCreateTag(registry, key)), entries));
        map.forEach(Reflection.HOLDER_REFERENCE::invokeBindTags);
        Reflection.MAPPED_REGISTRY.setAllTags(registry, Reflection.MAPPED_REGISTRY_TAG_SET.invokeFromMap(map2));
    }

    private static void resetTags(MappedRegistry<?> registry) {
        registry.getTags().forEach(entryList -> Reflection.HOLDER_SET.invokeBind(entryList, List.of()));
        Reflection.MAPPED_REGISTRY.getByKey(registry).values().forEach(entry -> Reflection.HOLDER_REFERENCE.invokeBindTags(entry, Set.of()));
    }
}

