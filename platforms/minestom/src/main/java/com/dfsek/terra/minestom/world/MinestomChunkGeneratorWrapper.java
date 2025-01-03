package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.config.ConfigPack;
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
    private ChunkGenerator generator;
    private final TerraMinestomWorld world;
    private ConfigPack pack;

    public MinestomChunkGeneratorWrapper(ChunkGenerator generator, TerraMinestomWorld world, ConfigPack pack) {
        this.generator = generator;
        this.world = world;
        this.pack = pack;
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
        chunk.writeRelative(unit.modifier());

        unit.fork(setter -> {
            MinestomProtoWorld protoWorld = new MinestomProtoWorld(cache, x, z, world, setter);

            for(GenerationStage stage : world.getPack().getStages()) {
                stage.populate(protoWorld);
            }
        });
    }

    public ConfigPack getPack() {
        return pack;
    }

    public void setPack(ConfigPack pack) {
        this.pack = pack;
        this.generator = pack.getGeneratorProvider().newInstance(pack);
    }

    public void displayStats() {
        cache.displayStats();
    }
}
