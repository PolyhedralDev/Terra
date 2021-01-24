package com.dfsek.terra.bukkit.generator;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.BiomeGrid;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.BlockPopulator;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import com.dfsek.terra.bukkit.world.BukkitBiomeGrid;
import com.dfsek.terra.bukkit.world.BukkitWorld;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockData;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BukkitChunkGenerator implements com.dfsek.terra.api.platform.world.generator.ChunkGenerator {
    private final ChunkGenerator delegate;

    public BukkitChunkGenerator(ChunkGenerator delegate) {
        this.delegate = delegate;
    }

    @Override
    public ChunkGenerator getHandle() {
        return delegate;
    }

    @Override
    public boolean isParallelCapable() {
        return delegate.isParallelCapable();
    }

    @Override
    public boolean shouldGenerateCaves() {
        return delegate.shouldGenerateCaves();
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return delegate.shouldGenerateDecorations();
    }

    @Override
    public boolean shouldGenerateMobs() {
        return delegate.shouldGenerateMobs();
    }

    @Override
    public boolean shouldGenerateStructures() {
        return delegate.shouldGenerateStructures();
    }

    @Override
    public ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome) {
        return new BukkitChunkData(delegate.generateChunkData(((BukkitWorld) world).getHandle(), random, x, z, ((BukkitBiomeGrid) biome).getHandle()));
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return delegate.getDefaultPopulators(((BukkitWorld) world).getHandle()).stream().map(BukkitPopulator::new).collect(Collectors.toList());
    }

    @Override
    public @Nullable TerraChunkGenerator getTerraGenerator() {
        return delegate instanceof BukkitChunkGeneratorWrapper ? ((BukkitChunkGeneratorWrapper) delegate).getHandle() : null;
    }

    public static class BukkitChunkData implements ChunkData {

        private final ChunkGenerator.ChunkData delegate;

        public BukkitChunkData(ChunkGenerator.ChunkData delegate) {
            this.delegate = delegate;
        }

        @Override
        public ChunkGenerator.ChunkData getHandle() {
            return delegate;
        }

        @Override
        public int getMaxHeight() {
            return delegate.getMaxHeight();
        }


        @Override
        public void setBlock(int x, int y, int z, @NotNull BlockData blockData) {
            delegate.setBlock(x, y, z, ((BukkitBlockData) blockData).getHandle());
        }


        @Override
        public @NotNull BlockData getBlockData(int x, int y, int z) {
            return BukkitBlockData.newInstance(delegate.getBlockData(x, y, z));
        }
    }
}
