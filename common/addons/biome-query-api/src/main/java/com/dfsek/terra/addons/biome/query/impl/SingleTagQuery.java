package com.dfsek.terra.addons.biome.query.impl;

import java.util.function.Predicate;

import com.dfsek.terra.addons.biome.query.BiomeQueryAPIAddon;
import com.dfsek.terra.api.world.biome.Biome;


public class SingleTagQuery implements Predicate<Biome> {
    private final String tag;
    private int tagIndex = -1;
    
    public SingleTagQuery(String tag) {
        this.tag = tag;
    }
    
    @Override
    public boolean test(Biome biome) {
        if(tagIndex < 0) {
            tagIndex = biome
                    .getContext()
                    .get(BiomeQueryAPIAddon.BIOME_TAG_KEY)
                    .getFlattener()
                    .index(tag);
        }
        return biome
                .getContext()
                .get(BiomeQueryAPIAddon.BIOME_TAG_KEY)
                .get(tagIndex);
    }
}
