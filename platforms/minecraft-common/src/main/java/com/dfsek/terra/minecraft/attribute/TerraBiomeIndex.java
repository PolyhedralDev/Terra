package com.dfsek.terra.minecraft.attribute;

import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.dfsek.terra.minecraft.attribute.BiomeTags.PlatformTagSource;


/**
 * Registered Terra biomes grouped by vanilla parent, and the platform tags of each.
 */
public final class TerraBiomeIndex {
    private final Map<Identifier, List<Identifier>> byParent = new LinkedHashMap<>();
    private final Map<Identifier, PlatformTagSource> tags = new LinkedHashMap<>();

    /**
     * Warning for a config whose lowercased biome ID is already taken. The config reuses that biome.
     */
    public static String sharedBiome(String pack, String config, Identifier terraId) {
        return "Pack \"" + pack + "\", config \"" + config + "\": reusing biome \"" + terraId
               + "\"; another config's ID matches apart from case";
    }

    public boolean contains(Identifier terraId) {
        return tags.containsKey(terraId);
    }

    public void record(Identifier terraId, Identifier parent, PlatformTagSource source) {
        byParent.computeIfAbsent(parent, i -> new ArrayList<>()).add(terraId);
        tags.put(terraId, source);
    }

    public void addAll(TerraBiomeIndex other) {
        other.byParent.forEach((parent, biomes) -> byParent.computeIfAbsent(parent, i -> new ArrayList<>()).addAll(biomes));
        tags.putAll(other.tags);
    }

    public List<Identifier> children(Identifier parent) {
        return byParent.getOrDefault(parent, List.of());
    }

    public Map<Identifier, List<Identifier>> byParent() {
        return Collections.unmodifiableMap(byParent);
    }

    public Map<Identifier, PlatformTagSource> tags() {
        return Collections.unmodifiableMap(tags);
    }
}
