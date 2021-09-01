package com.dfsek.terra.addons.ore.ores;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import com.dfsek.terra.api.util.generic.pair.ImmutablePair;


/**
 * Holds ordered list of ores mapped to their configs.
 */
public class OreHolder {
    private final List<Entry> entries = new ArrayList<>();
    
    public void forEach(BiConsumer<String, ImmutablePair<Ore, OreConfig>> consumer) {
        entries.forEach(entry -> consumer.accept(entry.getId(), ImmutablePair.of(entry.getOre(), entry.getConfig())));
    }
    
    public OreHolder add(Ore ore, OreConfig config, String id) {
        entries.add(new Entry(ore, config, id));
        return this;
    }
    
    private static final class Entry {
        private final Ore ore;
        private final OreConfig config;
        private final String id;
        
        private Entry(Ore ore, OreConfig config, String id) {
            this.ore = ore;
            this.config = config;
            this.id = id;
        }
        
        public OreConfig getConfig() {
            return config;
        }
        
        public Ore getOre() {
            return ore;
        }
        
        public String getId() {
            return id;
        }
    }
}
