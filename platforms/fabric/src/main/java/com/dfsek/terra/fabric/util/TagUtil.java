package com.dfsek.terra.fabric.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class TagUtil {
    private static final Logger logger = LoggerFactory.getLogger(TagUtil.class);
    
    private TagUtil() {
    
    }
    
    public static void registerTags(Registry<Biome> registry) {
        logger.info("Doing tag garbage....");
        Map<TagKey<Biome>, List<RegistryEntry<Biome>>> collect = registry
                .streamTagsAndEntries()
                .collect(HashMap::new,
                         (map, pair) ->
                                 map.put(pair.getFirst(), new ArrayList<>(pair.getSecond().stream().toList())),
                         HashMap::putAll);
        
        BiomeUtil
                .getTerraBiomeMap()
                .forEach((vb, terraBiomes) ->
                                 FabricUtil.getEntry(registry, vb)
                                           .ifPresentOrElse(vanilla -> terraBiomes
                                                                    .forEach(tb -> FabricUtil
                                                                            .getEntry(registry, tb)
                                                                            .ifPresentOrElse(
                                                                                    terra -> {
                                                                                        logger.debug(
                                                                                                vanilla.getKey()
                                                                                                       .orElseThrow()
                                                                                                       .getValue() +
                                                                                                " (vanilla for " +
                                                                                                terra.getKey()
                                                                                                     .orElseThrow()
                                                                                                     .getValue() +
                                                                                                ": " +
                                                                                                vanilla.streamTags()
                                                                                                       .toList());
                                                    
                                                                                        vanilla.streamTags()
                                                                                               .forEach(
                                                                                                       tag -> collect
                                                                                                               .computeIfAbsent(
                                                                                                                       tag,
                                                                                                                       t -> new ArrayList<>())
                                                                                                               .add(FabricUtil
                                                                                                                            .getEntry(
                                                                                                                                    registry,
                                                                                                                                    terra.getKey()
                                                                                                                                         .orElseThrow()
                                                                                                                                         .getValue())
                                                                                                                            .orElseThrow()));
                                                    
                                                                                    },
                                                                                    () -> logger.error(
                                                                                            "No such biome: {}",
                                                                                            tb))),
                                                            () -> logger.error("No vanilla biome: {}", vb)));
        
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
