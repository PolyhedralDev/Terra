package com.dfsek.terra.addons.biome.query.api;

import java.util.function.Predicate;

import com.dfsek.terra.addons.biome.query.impl.SingleTagQuery;
import com.dfsek.terra.api.world.biome.Biome;


public final class BiomeQueries {
    private BiomeQueries() {

    }
    
    public static Predicate<Biome> has(String tag) {
        return new SingleTagQuery(tag);
    }
}
