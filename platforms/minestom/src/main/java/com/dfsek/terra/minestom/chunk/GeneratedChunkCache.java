package com.dfsek.terra.minestom.chunk;

import com.dfsek.terra.api.util.generic.pair.Pair;

import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import net.minestom.server.world.DimensionType;


public class GeneratedChunkCache {
    private final LoadingCache<Pair<Integer, Integer>, CachedChunk> cache;
    private final DimensionType dimensionType;
    private final ChunkGenerator generator;
    private final ServerWorld world;
    private final BiomeProvider biomeProvider;

    public GeneratedChunkCache(DimensionType dimensionType, ChunkGenerator generator, ServerWorld world) {
        this.dimensionType = dimensionType;
        this.generator = generator;
        this.world = world;
        this.biomeProvider = world.getBiomeProvider();
        this.cache = Caffeine.newBuilder()
            .maximumSize(32)
            .build((Pair<Integer, Integer> key) -> generateChunk(key.getLeft(), key.getRight()));
    }

    private CachedChunk generateChunk(int x, int z) {
        CachedChunk chunk = new CachedChunk(dimensionType.minY(), dimensionType.maxY());
        generator.generateChunkData(
            chunk,
            world,
            biomeProvider,
            x, z
        );
        return chunk;
    }

    public CachedChunk at(int x, int y) {
        return cache.get(Pair.of(x, y));
    }
}
