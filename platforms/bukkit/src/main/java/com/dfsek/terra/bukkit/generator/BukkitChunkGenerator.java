package com.dfsek.terra.bukkit.generator;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.generator.ChunkData;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockData;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

public class BukkitChunkGenerator implements com.dfsek.terra.api.platform.world.generator.ChunkGenerator {
    private final ChunkGenerator delegate;

    public BukkitChunkGenerator(ChunkGenerator delegate) {
        this.delegate = delegate;
    }

    @Override
    public ChunkGenerator getHandle() {
        return delegate;
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
