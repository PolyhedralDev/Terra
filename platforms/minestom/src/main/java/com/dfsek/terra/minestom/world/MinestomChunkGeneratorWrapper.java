package com.dfsek.terra.minestom.world;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import net.minestom.server.instance.generator.GeneratorImpl.AreaModifierImpl;
import net.minestom.server.instance.generator.GeneratorImpl.SectionModifierImpl;
import net.minestom.server.instance.generator.UnitModifier;
import net.minestom.server.instance.palette.Palette;
import org.jetbrains.annotations.NotNull;

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
    private final MinestomUserDefinedBiomePool biomePool;
    private ChunkGenerator generator;
    private ConfigPack pack;

    public MinestomChunkGeneratorWrapper(
        ChunkGenerator generator,
        TerraMinestomWorld world,
        ConfigPack pack,
        MinestomUserDefinedBiomePool biomePool
    ) {
        this.generator = generator;
        this.world = world;
        this.pack = pack;
        this.biomePool = biomePool;
        this.biomeProvider = pack.getBiomeProvider();
        this.cache = new GeneratedChunkCache(world.getDimensionType(), generator, world, biomeProvider);
    }

    public ChunkGenerator getGenerator() {
        return generator;
    }

    @SuppressWarnings("UnstableApiUsage")
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

        AreaModifierImpl areaModifiers = (AreaModifierImpl) modifier;
        for(GenerationUnit section : areaModifiers.sections()) {
            SectionModifierImpl sectionModifier = (SectionModifierImpl) section.modifier();
            Palette biomes = sectionModifier.genSection().biomes();
            int minY = section.absoluteStart().blockY();
            for(int relativeX = 0; relativeX < 16; relativeX += 1) {
                int absoluteX = blockX + relativeX;
                for(int relativeZ = 0; relativeZ < 16; relativeZ += 1) {
                    int absoluteZ = blockZ + relativeZ;
                    for(int relativeY = 0; relativeY < 16; relativeY += 1) {
                        int absoluteY = minY + relativeY;

                        if(relativeX % 4 == 0 && relativeY % 4 == 0 && relativeZ % 4 == 0) {
                            UserDefinedBiome userDefinedBiome = biomePool.getBiome(
                                pack,
                                biomeProvider.getBiome(absoluteX, absoluteY, absoluteZ, world.getSeed())
                            );

                            int registryId = userDefinedBiome.registryId();
                            biomes.set(relativeX / 4, relativeY / 4, relativeZ / 4, registryId);
                        }
                    }
                }
            }
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
    }

    public void displayStats() {
        cache.displayStats();
    }

    @Override
    public ChunkGenerator getHandle() {
        return generator;
    }
}
