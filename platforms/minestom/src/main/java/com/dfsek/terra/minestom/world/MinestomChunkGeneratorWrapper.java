package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;

import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;
import com.dfsek.terra.minestom.api.filter.ChunkFilter;
import com.dfsek.terra.minestom.chunk.CachedChunk;
import com.dfsek.terra.minestom.chunk.GeneratedChunkCache;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import org.jetbrains.annotations.NotNull;


public class MinestomChunkGeneratorWrapper implements Generator {
    private final GeneratedChunkCache cache;
    private final ChunkGenerator generator;
    private final TerraMinestomWorld world;
    private final ChunkFilter filter;

    public MinestomChunkGeneratorWrapper(ChunkGenerator generator, TerraMinestomWorld world, ChunkFilter filter) {
        this.generator = generator;
        this.world = world;
        this.filter = filter;
        this.cache = new GeneratedChunkCache(world.getDimensionType(), generator, world);
    }

    public ChunkGenerator getGenerator() {
        return generator;
    }

    @Override
    public void generate(@NotNull GenerationUnit unit) {
        Point start = unit.absoluteStart();
        int x = start.chunkX();
        int z = start.chunkZ();
        CachedChunk chunk = cache.at(x, z);
        if(filter == null || filter.shouldPlaceTerrain(x, z))
            chunk.writeRelative(unit.modifier());

        if(filter == null || filter.shouldPlaceFeatures(x, z)) {
            unit.fork(setter -> {
                MinestomProtoWorld protoWorld = new MinestomProtoWorld(cache, x, z, world, setter);

                for(GenerationStage stage : world.getPack().getStages()) {
                    stage.populate(protoWorld);
                }
            });
        }
    }

    public void displayStats() {
        cache.displayStats();
    }
}
