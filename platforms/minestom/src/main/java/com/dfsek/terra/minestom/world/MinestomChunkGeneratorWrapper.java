package com.dfsek.terra.minestom.world;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import net.minestom.server.instance.generator.UnitModifier;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;
import com.dfsek.terra.api.world.chunk.generation.util.GeneratorWrapper;
import com.dfsek.terra.minestom.biome.MinestomUserDefinedBiomePool;
import com.dfsek.terra.minestom.biome.UserDefinedBiome;
import com.dfsek.terra.minestom.chunk.CachedChunk;
import com.dfsek.terra.minestom.chunk.GeneratedChunkCache;


public class MinestomChunkGeneratorWrapper implements Generator, GeneratorWrapper {
    private final GeneratedChunkCache cache;
    private final TerraMinestomWorld world;
    private final BiomeProvider biomeProvider;
    private final boolean doFineGrainedBiomes;
    private final MinestomUserDefinedBiomePool biomePool;
    private ChunkGenerator generator;
    private ConfigPack pack;

    public MinestomChunkGeneratorWrapper(
        Platform platform,
        ChunkGenerator generator,
        TerraMinestomWorld world,
        ConfigPack pack,
        MinestomUserDefinedBiomePool biomePool,
        boolean doFineGrainedBiomes
    ) {
        this.generator = generator;
        this.world = world;
        this.pack = pack;
        this.biomePool = biomePool;
        this.biomeProvider = pack.getBiomeProvider();
        this.doFineGrainedBiomes = doFineGrainedBiomes;
        this.cache = new GeneratedChunkCache(world.getDimensionType(), generator, world, biomeProvider);
        preloadBiomes();
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
        int minY = world.getMinHeight();
        int maxY = world.getMaxHeight();
        CachedChunk chunk = cache.at(x, z);
        UnitModifier modifier = unit.modifier();
        chunk.writeRelative(modifier);

        if(doFineGrainedBiomes) {
            for(int y = minY; y < maxY; y++) {
                for(int absoluteX = blockX; absoluteX < blockX + 16; absoluteX++) {
                    for(int absoluteZ = blockZ; absoluteZ < blockZ + 16; absoluteZ++) {
                        UserDefinedBiome userDefinedBiome = biomePool.getBiome(
                            biomeProvider.getBiome(absoluteX, y, absoluteZ, world.getSeed())
                        );
                        modifier.setBiome(absoluteX, y, absoluteZ, userDefinedBiome.registry());
                    }
                }
            }
        } else {
            // TODO: remove with feature flag once minestom fixed biome encoding
            UserDefinedBiome userDefinedBiome = biomePool.getBiome(biomeProvider.getBiome(blockX, 100, blockZ, world.getSeed()));
            modifier.fillBiome(userDefinedBiome.registry());
        }

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
        this.biomePool.invalidate();
        preloadBiomes();
    }

    private void preloadBiomes() {
        this.biomePool.preloadBiomes(world.getBiomeProvider().getBiomes());
    }

    public void displayStats() {
        cache.displayStats();
    }

    @Override
    public ChunkGenerator getHandle() {
        return generator;
    }
}
