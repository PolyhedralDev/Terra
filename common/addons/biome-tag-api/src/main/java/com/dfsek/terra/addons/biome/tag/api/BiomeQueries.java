package com.dfsek.terra.addons.biome.tag.api;

import com.dfsek.terra.addons.biome.tag.impl.SingleTagQuery;
import com.dfsek.terra.api.world.biome.Biome;

import java.util.function.Predicate;


public final class BiomeQueries {
    private BiomeQueries() {
    
    }
    
    public static Predicate<Biome> has(String tag) {
        return new SingleTagQuery(tag);
    }
}
