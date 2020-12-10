package com.dfsek.terra.api.bukkit.generator;

import com.dfsek.terra.api.bukkit.BukkitBiomeGrid;
import com.dfsek.terra.api.bukkit.BukkitWorld;
import com.dfsek.terra.api.bukkit.world.block.BukkitBlockData;
import com.dfsek.terra.api.generic.world.block.BlockData;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class BukkitChunkGeneratorWrapper extends ChunkGenerator {
    private final com.dfsek.terra.api.generic.generator.ChunkGenerator delegate;

    public BukkitChunkGeneratorWrapper(com.dfsek.terra.api.generic.generator.ChunkGenerator delegate) {
        this.delegate = delegate;
    }

    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome) {
        BukkitWorld bukkitWorld = new BukkitWorld(world);
        BukkitChunkData data = new BukkitChunkData(createChunkData(world));
        delegate.generateChunkData(bukkitWorld, random, x, z, new BukkitBiomeGrid(biome), data);
        return data.getHandle();
    }

    public static class BukkitChunkData implements com.dfsek.terra.api.generic.generator.ChunkGenerator.ChunkData {

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
            return new BukkitBlockData(delegate.getBlockData(x, y, z));
        }
    }
}
