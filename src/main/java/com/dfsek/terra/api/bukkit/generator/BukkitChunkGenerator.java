package com.dfsek.terra.api.bukkit.generator;

import com.dfsek.terra.api.bukkit.BukkitBiomeGrid;
import com.dfsek.terra.api.bukkit.BukkitBlockData;
import com.dfsek.terra.api.bukkit.BukkitWorld;
import com.dfsek.terra.api.generic.BlockData;
import com.dfsek.terra.api.generic.generator.BlockPopulator;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public abstract class BukkitChunkGenerator extends ChunkGenerator implements com.dfsek.terra.api.generic.generator.ChunkGenerator {


    @Override
    public ChunkGenerator.@NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome) {
        BukkitChunkData data = new BukkitChunkData(createChunkData(((BukkitWorld) world).getHandle()));
        generateChunkData(new BukkitWorld(world), random, x, z, new BukkitBiomeGrid(biome), data);
        return data.getHandle();
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(com.dfsek.terra.api.generic.world.World world) {
        return null;
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
