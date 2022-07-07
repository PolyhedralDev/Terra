package com.dfsek.terra.addons.biome.query.impl;

import java.util.List;


public class BiomeTagFlattener {
    private final List<String> tags;
    
    public BiomeTagFlattener(List<String> tags) {
        this.tags = tags;
    }
    
    public int index(String tag) {
        return tags.indexOf(tag);
    }
    
    public int size() {
        return tags.size();
    }
}
