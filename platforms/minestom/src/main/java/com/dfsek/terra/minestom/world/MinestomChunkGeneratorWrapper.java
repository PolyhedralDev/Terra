package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.PlatformBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;

import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;
import com.dfsek.terra.api.world.chunk.generation.util.GeneratorWrapper;
import com.dfsek.terra.minestom.biome.MinestomBiome;
import com.dfsek.terra.minestom.chunk.CachedChunk;
import com.dfsek.terra.minestom.chunk.GeneratedChunkCache;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import net.minestom.server.instance.generator.UnitModifier;
import net.minestom.server.registry.DynamicRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class MinestomChunkGeneratorWrapper implements Generator, GeneratorWrapper {
    private final GeneratedChunkCache cache;
    private ChunkGenerator generator;
    private final TerraMinestomWorld world;
    private final BiomeProvider biomeProvider;
    private ConfigPack pack;

    public MinestomChunkGeneratorWrapper(Platform platform, ChunkGenerator generator, TerraMinestomWorld world, ConfigPack pack) {
        this.generator = generator;
        this.world = world;
        this.pack = pack;

        biomeProvider = pack.getBiomeProvider().caching(platform);
        this.cache = new GeneratedChunkCache(world.getDimensionType(), generator, world, biomeProvider);
    }

    public ChunkGenerator getGenerator() {
        return generator;
    }

    @Override
    public void generate(@NotNull GenerationUnit unit) {
        Point start = unit.absoluteStart();
        int x = start.chunkX();
        int z = start.chunkZ();
        int blockX = start.blockX();
        int blockZ = start.blockZ();
        CachedChunk chunk = cache.at(x, z);
        UnitModifier modifier = unit.modifier();
        chunk.writeRelative(modifier);

//        for(int dx = 0; dx < 16; dx++) {
//            for(int dz = 0; dz < 16; dz++) {
//                int globalX = dx + blockX;
//                int globalZ = dz + blockZ;
//                biomeProvider.getColumn(globalX, globalZ, world).forEach((y, biome) -> {
//                    MinestomBiome platformBiome = (MinestomBiome) biome.getPlatformBiome();
//                modifier.setBiome(globalX, 0, globalZ, DynamicRegistry.Key.of("minecraft:the_void"));
//                });
//            }
//        }

        unit.fork(setter -> {
            MinestomProtoWorld protoWorld = new MinestomProtoWorld(cache, x, z, world, setter);

            var stages = world.getPack().getStages();

            if(x==0 && z==0) {
                System.out.println(stages);
                System.out.println(protoWorld.getBlockState(-4, 73, -6).getAsString());
            }

            for(GenerationStage stage : world.getPack().getStages()) {
                stage.populate(protoWorld);
                if(x==0 && z==0) System.out.println(protoWorld.getBlockState(-4, 73, -6).getAsString());
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

    @Override
    public ChunkGenerator getHandle() {
        return generator;
    }
}
