package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;

import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;
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

    public MinestomChunkGeneratorWrapper(ChunkGenerator generator, TerraMinestomWorld world) {
        this.generator = generator;
        this.world = world;
        this.cache = new GeneratedChunkCache(world.getDimensionType(), generator, world);
    }

    public ChunkGenerator getGenerator() {
        return generator;
    }

    @Override
    public void generate(@NotNull GenerationUnit unit) {
        Point start = unit.absoluteStart();
        CachedChunk chunk = cache.at(start.chunkX(), start.chunkZ());

        //chunk.writeRelative(unit.modifier());

        unit.fork(setter -> {
            MinestomProtoWorld protoWorld = new MinestomProtoWorld(
                cache,
                start.chunkX(),
                start.chunkZ(),
                world,
                setter
            );

            for(GenerationStage stage : world.getPack().getStages()) {
                stage.populate(protoWorld);
            }
        });
    }
}
