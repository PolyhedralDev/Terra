package com.dfsek.terra.api.world.biome.tag;

import com.dfsek.seismic.util.UnsafeUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.dfsek.terra.api.world.biome.Biome;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


/**
 * Biome IDs are not stable across loads; never persist one.
 */
public final class BiomeTag implements Predicate<Biome> {
    // Lets has() on an unsealed tag return false for IDs below 512.
    private static final long[] UNSEALED = new long[8];

    private final String name;
    private final BiomeTagIndex index;

    // Not volatile: published by the index's volatile sealed flag, or by the index lock for tags created after seal.
    private long[] bits = UNSEALED;

    BiomeTag(String name, BiomeTagIndex index) {
        this.name = name;
        this.index = index;
    }

    @NotNull
    @Contract(pure = true)
    public String name() {
        return name;
    }

    /**
     * No bounds checks. Only call after the owning {@link BiomeTagIndex} is sealed, with IDs from that index.
     */
    public boolean has(int biomeId) {
        long word = UnsafeUtils.UNSAFE.getLong(bits,
            UnsafeUtils.LONG_ARRAY_BASE + ((long) (biomeId >>> 6) << UnsafeUtils.LONG_ARRAY_SHIFT));
        return ((word >>> biomeId) & 1L) != 0L;
    }

    /**
     * Doesn't detect a biome from another pack whose ID is in range.
     *
     * @throws IllegalArgumentException if sealed and the biome's ID is outside this tag's bitset
     */
    @Override
    public boolean test(Biome biome) {
        if(!index.isSealed()) { // volatile read: also publishes bits built by seal
            return biome.getTags().contains(name);
        }
        int id = biome.getIntID();
        if(id < 0 || id >= (bits.length << 6)) {
            throw new IllegalArgumentException("biome id " + id + " is not from this tag's pack");
        }
        return has(id);
    }

    long[] bits() {
        return bits;
    }

    void build(int size, List<Set<String>> tagSets) {
        // Whole 64-byte lines: every ID below size is in bounds.
        long[] built = new long[((size + 511) >>> 9) << 3];
        for(int id = 1; id < size; id++) {
            if(tagSets.get(id).contains(name)) {
                built[id >>> 6] |= 1L << id;
            }
        }
        this.bits = built;
    }
}
