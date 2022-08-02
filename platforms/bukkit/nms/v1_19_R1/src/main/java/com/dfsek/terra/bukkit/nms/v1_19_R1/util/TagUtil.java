package com.dfsek.terra.bukkit.nms.v1_19_R1.util;


import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
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
    
    private static <T> Map<TagKey<T>, List<Holder<T>>> tagsToMutableMap(Registry<T> registry) {
        return registry
                .getTags()
                .collect(HashMap::new,
                         (map, pair) ->
                                 map.put(pair.getFirst(), new ArrayList<>(pair.getSecond().stream().toList())),
                         HashMap::putAll);
    }
    
    public static void registerBiomeTags(Registry<Biome> registry) {
        logger.info("Doing data-driven biome tag garbage....");
        logger.info("who let this data drive?");
        Map<TagKey<Biome>, List<Holder<Biome>>> collect = tagsToMutableMap(registry);
        
        BiomeUtil.TERRA_BIOME_TAG_MAP.forEach((tag, biomeList) -> {
            collect.getOrDefault(tag, new ArrayList<>())
                   .addAll(biomeList.stream()
                                    .map(registry::getOptional)
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .map(Holder::direct)
                                    .toList());
        });
        
        registry.resetTags();
        registry.bindTags(ImmutableMap.copyOf(collect));
        
        if(logger.isDebugEnabled()) {
            registry.holders()
                    .map(e -> e.key().location() + ": " +
                              e.tags().reduce("", (s, t) -> t.location() + ", " + s, String::concat))
                    .forEach(logger::debug);
        }
    }
}
