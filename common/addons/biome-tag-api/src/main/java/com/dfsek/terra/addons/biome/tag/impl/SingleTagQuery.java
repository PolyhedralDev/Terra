package com.dfsek.terra.addons.biome.tag.impl;

import com.dfsek.terra.addons.biome.tag.BiomeTagAPIAddon;
import com.dfsek.terra.api.world.biome.Biome;

import java.util.function.Predicate;


public class SingleTagQuery implements Predicate<Biome> {
    private int tagIndex = -1;
    private final String tag;
    
    public SingleTagQuery(String tag) {
        this.tag = tag;
    }
    
    @Override
    public boolean test(Biome biome) {
        if(tagIndex < 0) {
            tagIndex = biome
                    .getContext()
                    .get(BiomeTagAPIAddon.BIOME_TAG_KEY)
                    .getFlattener()
                    .index(tag);
        }
        return biome
                .getContext()
                .get(BiomeTagAPIAddon.BIOME_TAG_KEY)
                .get(tagIndex);
    }
}
