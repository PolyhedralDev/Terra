package com.dfsek.terra.mod.util;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader.LoadResult;
import net.minecraft.tags.WorldPresetTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraft.world.timeline.Timeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dfsek.terra.minecraft.attribute.BiomeTags;
import com.dfsek.terra.minecraft.attribute.MinecraftTimelines;


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

    public static void registerTimelineTags(Registry<Timeline> registry) {
        logger.info("Registering timeline tags.");
        Map<TagKey<Timeline>, List<Holder<Timeline>>> collect = tagsToMutableMap(registry);
        MinecraftTimelines.fillTags(collect, registry);
        registry.prepareTagReload(new LoadResult<>(registry.key(), collect)).apply();
    }

    public static void registerBiomeTags(Registry<Biome> registry) {
        logger.info("Doing biome tag garbage....");
        Map<TagKey<Biome>, List<Holder<Biome>>> collect = tagsToMutableMap(registry);

        BiomeTags.addTerraBiomes(collect, id -> MinecraftUtil.getEntry(registry, id), BiomeUtil.terraBiomes());

        registry.prepareTagReload(new LoadResult<>(registry.key(), collect)).apply();

        if(logger.isDebugEnabled()) {
            registry.listElements()
                .map(e -> e.key().identifier() + ": " +
                          e.tags().reduce("", (s, t) -> t.location() + ", " + s, String::concat))
                .forEach(logger::debug);
        }
    }
}
