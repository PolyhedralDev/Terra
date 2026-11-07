package com.dfsek.terra.mod.util;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader.LoadResult;
import net.minecraft.tags.WorldPresetTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public final class TagUtil {
    private static final Logger logger = LoggerFactory.getLogger(TagUtil.class);

    private TagUtil() {

    }

    private static <T> Map<TagKey<T>, List<Holder<T>>> tagsToMutableMap(Registry<T> registry) {
        return registry.getTags().collect(HashMap::new,
            (map, tag) -> map.put(tag.key(), tag.stream().collect(Collectors.toList())),
            HashMap::putAll);
    }

    public static void registerWorldPresetTags(Registry<WorldPreset> registry) {
        logger.info("Registering Preset Tags.");
        Map<TagKey<WorldPreset>, List<Holder<WorldPreset>>> collect = tagsToMutableMap(registry);

        PresetUtil
            .getPresets()
            .forEach(pair -> MinecraftUtil
                .getEntry(registry, pair.getLeft())
                .ifPresentOrElse(
                    preset -> {
                        boolean useExtendedTag = pair.getRight(); // Get the boolean value from the pair
                        collect
                            .computeIfAbsent(useExtendedTag ? WorldPresetTags.EXTENDED : WorldPresetTags.NORMAL, tag -> new ArrayList<>())
                            .add(preset);
                    },
                    () -> logger.error("Preset {} does not exist!", pair.getLeft())));

        registry.prepareTagReload(new LoadResult<>(registry.key(), collect)).apply();


        if(logger.isDebugEnabled()) {
            registry.listElements()
                .map(e -> e.key().identifier() + ": " +
                          e.tags().reduce("", (s, t) -> t.location() + ", " + s, String::concat))
                .forEach(logger::debug);
        }
    }

    public static void registerBiomeTags(Registry<Biome> registry) {
        logger.info("Doing biome tag garbage....");
        Map<TagKey<Biome>, List<Holder<Biome>>> collect = tagsToMutableMap(registry);

        BiomeUtil
            .getTerraBiomeMap()
            .forEach((vb, terraBiomes) ->
                MinecraftUtil
                    .getEntry(registry, vb)
                    .ifPresentOrElse(
                        vanilla -> terraBiomes
                            .forEach(tb -> MinecraftUtil
                                .getEntry(registry, tb)
                                .ifPresentOrElse(
                                    terra -> {
                                        logger.debug(
                                            vanilla.unwrapKey()
                                                .orElseThrow()
                                                .identifier() +
                                            " (vanilla for " +
                                            terra.unwrapKey()
                                                .orElseThrow()
                                                .identifier() +
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
                                    () -> logger.error(
                                        "No such biome: {}",
                                        tb))),
                        () -> logger.error("No vanilla biome: {}", vb)));

        registry.prepareTagReload(new LoadResult<>(registry.key(), collect)).apply();

        if(logger.isDebugEnabled()) {
            registry.listElements()
                .map(e -> e.key().identifier() + ": " +
                          e.tags().reduce("", (s, t) -> t.location() + ", " + s, String::concat))
                .forEach(logger::debug);
        }
    }
}
