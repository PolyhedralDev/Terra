package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;

import com.dfsek.terra.api.world.chunk.generation.stage.GenerationStage;
import com.dfsek.terra.api.world.chunk.generation.util.GeneratorWrapper;
import com.dfsek.terra.minestom.biome.MinestomCustomBiomeFactory;
import com.dfsek.terra.minestom.biome.MinestomCustomBiomePool;
import com.dfsek.terra.minestom.biome.NativeBiome;
import com.dfsek.terra.minestom.chunk.CachedChunk;
import com.dfsek.terra.minestom.chunk.GeneratedChunkCache;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import net.minestom.server.instance.generator.UnitModifier;
import org.jetbrains.annotations.NotNull;


public class MinestomChunkGeneratorWrapper implements Generator, GeneratorWrapper {
    private final GeneratedChunkCache cache;
    private ChunkGenerator generator;
    private final TerraMinestomWorld world;
    private final BiomeProvider biomeProvider;
    private ConfigPack pack;
    private final MinestomCustomBiomePool biomePool;

    public MinestomChunkGeneratorWrapper(
        Platform platform,
        ChunkGenerator generator,
        TerraMinestomWorld world,
        ConfigPack pack,
        MinestomCustomBiomePool biomePool
    ) {
        this.generator = generator;
        this.world = world;
        this.pack = pack;
        this.biomePool = biomePool;
        biomeProvider = pack.getBiomeProvider().caching(platform);
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
        CachedChunk chunk = cache.at(x, z);
        UnitModifier modifier = unit.modifier();
        chunk.writeRelative(modifier);

        NativeBiome nativeBiome = biomePool.getBiome(biomeProvider.getBiome(blockX, 100, blockZ, world.getSeed()));
        modifier.fillBiome(nativeBiome.registry());

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
        for(Player player : MinecraftServer.getConnectionManager().getOnlinePlayers()) {
            player.startConfigurationPhase();
        }
    }

    public void displayStats() {
        cache.displayStats();
    }

    @Override
    public ChunkGenerator getHandle() {
        return generator;
    }
}
