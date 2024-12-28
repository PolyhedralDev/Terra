package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;

import com.dfsek.terra.minestom.chunk.MinestomProtoChunk;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import org.jetbrains.annotations.NotNull;


public class MinestomChunkGeneratorWrapper implements Generator {
    private final ChunkGenerator generator;
    private final TerraMinestomWorld world;

    public MinestomChunkGeneratorWrapper(ChunkGenerator generator, TerraMinestomWorld world) {
        this.generator = generator;
        this.world = world;
    }

    public ChunkGenerator getGenerator() {
        return generator;
    }

    @Override
    public void generate(@NotNull GenerationUnit generationUnit) {
        MinestomProtoChunk protoChunk = new MinestomProtoChunk(
            world.getMaxHeight(),
            world.getMinHeight(),
            generationUnit.modifier()
        );
        Point start = generationUnit.absoluteStart();
        generator.generateChunkData(protoChunk, world, world.getBiomeProvider(), start.chunkX(), start.chunkZ());
    }
}
