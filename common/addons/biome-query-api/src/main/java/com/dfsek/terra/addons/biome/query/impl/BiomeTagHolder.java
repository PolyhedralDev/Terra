package com.dfsek.terra.addons.biome.query.impl;

import com.dfsek.terra.api.properties.Properties;
import com.dfsek.terra.api.world.biome.Biome;


public class BiomeTagHolder implements Properties {
    private final boolean[] tags;
    private final BiomeTagFlattener flattener;
    
    public BiomeTagHolder(Biome biome, BiomeTagFlattener flattener) {
        this.tags = new boolean[flattener.size()];
        this.flattener = flattener;
        for(String tag : biome.getTags()) {
            tags[flattener.index(tag)] = true;
        }
    }
    
    boolean get(int index) {
        return tags[index];
    }
    
    public BiomeTagFlattener getFlattener() {
        return flattener;
    }
}
