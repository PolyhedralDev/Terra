package com.dfsek.terra.minecraft.attribute;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import com.dfsek.terra.api.registry.key.RegistryKey;
import com.dfsek.terra.world.attribute.platform.AttributeReport;


public final class BiomeTags {
    private static final Logger logger = LoggerFactory.getLogger(BiomeTags.class);

    private BiomeTags() {

    }

    public record PlatformTagSource(AttributeReport report, String config, Set<RegistryKey> tags) { }

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
