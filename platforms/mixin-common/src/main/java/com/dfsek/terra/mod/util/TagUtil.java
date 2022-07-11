package com.dfsek.terra.mod.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.tag.TagKey;
import net.minecraft.tag.WorldPresetTags;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.WorldPreset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public final class TagUtil {
    private static final Logger logger = LoggerFactory.getLogger(TagUtil.class);
    
    private TagUtil() {
    
    }
    
    private static <T> Map<TagKey<T>, List<RegistryEntry<T>>> tagsToMutableMap(Registry<T> registry) {
        return registry
                .streamTagsAndEntries()
                .collect(HashMap::new,
                         (map, pair) ->
                                 map.put(pair.getFirst(), new ArrayList<>(pair.getSecond().stream().toList())),
                         HashMap::putAll);
    }
    
    public static void registerWorldPresetTags(Registry<WorldPreset> registry) {
        logger.info("Doing preset tag garbage....");
        Map<TagKey<WorldPreset>, List<RegistryEntry<WorldPreset>>> collect = tagsToMutableMap(registry);
        
        PresetUtil
                .getPresets()
                .forEach(id -> MinecraftUtil
                        .getEntry(registry, id)
                        .ifPresentOrElse(
                                preset -> collect
                                        .computeIfAbsent(WorldPresetTags.NORMAL, tag -> new ArrayList<>())
                                        .add(preset),
                                () -> logger.error("Preset {} does not exist!", id)));
        
        registry.clearTags();
        registry.populateTags(ImmutableMap.copyOf(collect));
    }
    
    public static void registerBiomeTags(Registry<Biome> registry) {
        logger.info("Doing data-driven biome tag garbage....");
        logger.info("who let this data drive?");
        Map<TagKey<Biome>, List<RegistryEntry<Biome>>> collect = tagsToMutableMap(registry);
    
        MinecraftUtil.TERRA_BIOME_TAG_MAP.forEach((tag, biomeList) -> {
            collect.getOrDefault(tag, new ArrayList<>())
                   .addAll(biomeList.stream()
                                    .map(registry::getOrEmpty)
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .map(RegistryEntry::of)
                                    .toList());
        });
    
        registry.clearTags();
        registry.populateTags(ImmutableMap.copyOf(collect));
    
        if(logger.isDebugEnabled()) {
            registry.streamEntries()
                    .map(e -> e.registryKey().getValue() + ": " +
                              e.streamTags().reduce("", (s, t) -> t.id() + ", " + s, String::concat))
                    .forEach(logger::debug);
        }
    }
}
