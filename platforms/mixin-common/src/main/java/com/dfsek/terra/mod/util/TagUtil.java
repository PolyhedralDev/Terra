package com.dfsek.terra.mod.util;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.mod.attribute.AttributeReport;
import com.dfsek.terra.mod.attribute.MinecraftTimelines;
import com.dfsek.terra.mod.attribute.VanillaIds;


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

    public record PlatformTagSource(AttributeReport report, String config, Set<RegistryKey> tags) { }

    public static void registerBiomeTags(Registry<Biome> registry) {
        logger.info("Doing biome tag garbage....");
        Map<TagKey<Biome>, List<Holder<Biome>>> collect = tagsToMutableMap(registry);

        addTerraBiomes(collect, id -> MinecraftUtil.getEntry(registry, id), BiomeUtil.getTerraBiomeMap(),
            BiomeUtil.getTerraBiomePlatformTags());

        registry.prepareTagReload(new LoadResult<>(registry.key(), collect)).apply();

        if(logger.isDebugEnabled()) {
            registry.listElements()
                .map(e -> e.key().identifier() + ": " +
                          e.tags().reduce("", (s, t) -> t.location() + ", " + s, String::concat))
                .forEach(logger::debug);
        }
    }

    public static void addTerraBiomes(Map<TagKey<Biome>, List<Holder<Biome>>> collect,
                                      Function<Identifier, Optional<Holder<Biome>>> lookup,
                                      Map<Identifier, List<Identifier>> byParent, Map<Identifier, PlatformTagSource> platformTags) {
        byParent.forEach((vb, terraBiomes) ->
            lookup.apply(vb).ifPresentOrElse(
                vanilla -> terraBiomes.forEach(tb -> lookup.apply(tb).ifPresentOrElse(
                    terra -> {
                        logger.debug(vanilla.unwrapKey().orElseThrow().identifier() + " (vanilla for " +
                                     terra.unwrapKey().orElseThrow().identifier() + ": " + vanilla.tags().toList());
                        vanilla.tags().forEach(tag -> collect.computeIfAbsent(tag, t -> new ArrayList<>()).add(terra));
                    },
                    () -> logger.error("No such biome: {}", tb))),
                () -> logger.error("No vanilla biome: {}", vb)));

        Set<TagKey<Biome>> existing = new HashSet<>(collect.keySet());
        platformTags.forEach((tb, source) -> {
            if(source.tags().isEmpty()) return;
            Optional<Holder<Biome>> terra = lookup.apply(tb);
            if(terra.isEmpty()) {
                logger.error("No such biome: {}", tb);
                return;
            }
            for(RegistryKey key : source.tags()) {
                Identifier id;
                try {
                    id = VanillaIds.identifier(key);
                } catch(IllegalArgumentException e) {
                    source.report().ignored(source.config(), key.toString(), e.getMessage());
                    continue;
                }
                TagKey<Biome> tag = TagKey.create(Registries.BIOME, id);
                if(!existing.contains(tag) && id.getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
                    source.report().tagCreated(source.config(), key.toString());
                }
                List<Holder<Biome>> biomes = collect.computeIfAbsent(tag, t -> new ArrayList<>());
                if(!biomes.contains(terra.get())) biomes.add(terra.get());
            }
        });
    }
}
