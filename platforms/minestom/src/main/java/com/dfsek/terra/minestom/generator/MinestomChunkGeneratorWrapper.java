package com.dfsek.terra.minestom.generator;

import com.dfsek.terra.api.platform.world.generator.GeneratorWrapper;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import com.dfsek.terra.minestom.world.MinestomBiomeGrid;
import com.dfsek.terra.minestom.world.MinestomBlockPopulatorWrapper;
import com.dfsek.terra.minestom.world.MinestomChunkData;
import com.dfsek.terra.minestom.world.MinestomWorld;
import com.dfsek.terra.world.generation.generators.DefaultChunkGenerator3D;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MinestomChunkGeneratorWrapper implements GeneratorWrapper, ChunkGenerator {
    private final DefaultChunkGenerator3D chunkGenerator3D;
    private final Instance instance;
    private final List<ChunkPopulator> populators = new ArrayList<>();

    public MinestomChunkGeneratorWrapper(DefaultChunkGenerator3D chunkGenerator3D, Instance instance) {
        this.chunkGenerator3D = chunkGenerator3D;
        this.instance = instance;
        chunkGenerator3D.getPopulators().forEach(terraBlockPopulator -> populators.add(new MinestomBlockPopulatorWrapper(terraBlockPopulator, instance)));
    }

    @Override
    public TerraChunkGenerator getHandle() {
        return chunkGenerator3D;
    }

    @Override
    public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
        chunkGenerator3D.generateChunkData(new MinestomWorld(instance), new FastRandom(), chunkX, chunkZ, new MinestomChunkData(batch));
    }

    @Override
    public void fillBiomes(@NotNull Biome[] biomes, int chunkX, int chunkZ) {
        chunkGenerator3D.generateBiomes(new MinestomWorld(instance), new FastRandom(), chunkX, chunkZ, new MinestomBiomeGrid(biomes, chunkX, chunkZ));
    }

    @Override
    public @Nullable List<ChunkPopulator> getPopulators() {
        return populators;
    }
}
