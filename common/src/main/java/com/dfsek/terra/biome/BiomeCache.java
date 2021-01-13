package com.dfsek.terra.biome;

import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.biome.pipeline.BiomeHolder;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jetbrains.annotations.NotNull;

public class BiomeCache {
    LoadingCache<Vector2, BiomeHolder> cache;

    public BiomeCache() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(1024)
                .build(
                        new CacheLoader<Vector2, BiomeHolder>() {
                            @Override
                            public BiomeHolder load(@NotNull Vector2 key) throws Exception {
                                return null;
                            }
                        }
                );
    }
}
