package com.dfsek.terra.internal;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.platform.world.BiomeGrid;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkData;
import com.dfsek.terra.api.world.biome.provider.BiomeProvider;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.world.generation.math.samplers.Sampler;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class InternalGeneratorWrapper implements TerraChunkGenerator {
    private final TerraChunkGenerator generator;

    public InternalGeneratorWrapper(TerraChunkGenerator generator) {
        this.generator = generator;
    }

//    @Override
//    public Object getHandle() {
//        return generator;
//    }

    @Override
    public ChunkData generateChunkData(@NotNull World world, Random random, int x, int z,
                                       ChunkData original) {
        return null;
    }

    @Override
    public void generateBiomes(@NotNull World world, @NotNull Random random, int x, int z,
                               @NotNull BiomeGrid biome) {

    }

    @Override
    public boolean isParallelCapable() {
        return true;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return true;
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return true;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return true;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return true;
    }

    @Override
    public ConfigPack getConfigPack() {
        return null;
    }

    @Override
    public TerraPlugin getMain() {
        return null;
    }

    @Override
    public Sampler createSampler(int chunkX, int chunkZ,
                                 BiomeProvider provider,
                                 World world, int elevationSmooth) {
        return null;
    }

//    @Override
//    public ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome) {
//        throw new UnsupportedOperationException(); // gen is directly handled by Generator
//    }
//
//    @Override
//    public List<BlockPopulator> getDefaultPopulators(World world) {
//        return Collections.emptyList();
//    }
//
//    @Override
//    public @Nullable TerraChunkGenerator getTerraGenerator() {
//        return generator;
//    }
}
