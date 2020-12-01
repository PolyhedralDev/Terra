package com.dfsek.terra.generation.items.ores;

import org.polydev.gaea.util.GlueList;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Holds ordered list of ores mapped to their configs.
 */
public class OreHolder {
    private final List<Entry> entries = new GlueList<>();

    public void forEach(BiConsumer<Ore, OreConfig> consumer) {
        entries.forEach(entry -> consumer.accept(entry.getOre(), entry.getConfig()));
    }

    public OreHolder add(Ore ore, OreConfig config) {
        entries.add(new Entry(ore, config));
        return this;
    }

    private static final class Entry {
        private final Ore ore;
        private final OreConfig config;

        private Entry(Ore ore, OreConfig config) {
            this.ore = ore;
            this.config = config;
        }

        public OreConfig getConfig() {
            return config;
        }

        public Ore getOre() {
            return ore;
        }
    }
}
