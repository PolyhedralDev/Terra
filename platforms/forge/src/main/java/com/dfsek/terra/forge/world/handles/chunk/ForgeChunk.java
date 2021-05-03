package com.dfsek.terra.forge.world.handles.chunk;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.forge.world.block.ForgeBlockData;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class ForgeChunk implements Chunk {
    private final net.minecraft.world.chunk.Chunk chunk;

    public ForgeChunk(net.minecraft.world.chunk.Chunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public int getX() {
        return chunk.getPos().x;
    }

    @Override
    public int getZ() {
        return chunk.getPos().z;
    }

    @Override
    public World getWorld() {
        return null;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return null;
    }

    @Override
    public net.minecraft.world.chunk.Chunk getHandle() {
        return chunk;
    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockData blockData) {
        chunk.setBlockState(new BlockPos(x, y, z), ((ForgeBlockData) blockData).getHandle(), false);
    }

    @Override
    public @NotNull BlockData getBlockData(int x, int y, int z) {
        return getBlock(x, y, z).getBlockData();
    }
}
