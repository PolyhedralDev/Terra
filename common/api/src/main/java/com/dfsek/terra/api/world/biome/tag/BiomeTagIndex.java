package com.dfsek.terra.api.world.biome.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


/**
 * ID 0 is reserved. Biomes register during pack load; the platform seals the index once the pack is built.
 */
public final class BiomeTagIndex {
    private final ConcurrentHashMap<String, BiomeTag> tags = new ConcurrentHashMap<>();
    private final List<Set<String>> tagSets = new ArrayList<>(); // guarded by this
    private volatile boolean sealed = false;

    public BiomeTagIndex() {
        tagSets.add(Set.of());
    }

    /**
     * @return a dense ID, at least 1
     *
     * @throws IllegalStateException if sealed
     */
    public synchronized int register(Set<String> biomeTags) {
        if(sealed) throw new IllegalStateException("BiomeTagIndex is sealed");
        tagSets.add(Set.copyOf(biomeTags));
        return tagSets.size() - 1;
    }

    /**
     * Never released: resolve each tag once at pack load, not per sample.
     */
    @NotNull
    public BiomeTag tag(String name) {
        BiomeTag existing = tags.get(name);
        // The volatile read of sealed publishes the bits; before seal, the lock does.
        if(existing != null && sealed) return existing;
        synchronized(this) {
            BiomeTag tag = tags.get(name);
            if(tag == null) {
                tag = new BiomeTag(name, this);
                if(sealed) tag.build(tagSets.size(), tagSets);
                tags.put(name, tag);
            }
            return tag;
        }
    }

    @ApiStatus.Internal
    public synchronized void seal() {
        if(sealed) return;
        int size = tagSets.size();
        for(BiomeTag tag : tags.values()) tag.build(size, tagSets);
        sealed = true;
    }

    @Contract(pure = true)
    public boolean isSealed() {
        return sealed;
    }

    /**
     * @return number of IDs including the reserved 0
     */
    public synchronized int size() {
        return tagSets.size();
    }
}
