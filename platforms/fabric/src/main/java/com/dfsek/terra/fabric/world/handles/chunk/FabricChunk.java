package com.dfsek.terra.fabric.world.handles.chunk;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.fabric.world.block.FabricBlock;
import com.dfsek.terra.fabric.world.block.FabricBlockData;
import com.dfsek.terra.fabric.world.handles.world.FabricWorldHandle;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class FabricChunk implements Chunk {
    private final net.minecraft.world.chunk.Chunk chunk;
    private final FabricWorldHandle worldHandle;

    public FabricChunk(FabricWorldHandle worldHandle, net.minecraft.world.chunk.Chunk chunk) {
        this.worldHandle = worldHandle;
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
        return worldHandle;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        BlockPos pos = new BlockPos(x + (chunk.getPos().x << 4), y, z + (chunk.getPos().z << 4));
        return new FabricBlock(pos, worldHandle.getWorld());
    }

    @Override
    public net.minecraft.world.chunk.Chunk getHandle() {
        return chunk;
    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockData blockData) {
        chunk.setBlockState(new BlockPos(x, y, z), ((FabricBlockData) blockData).getHandle(), false);
    }

    @Override
    public @NotNull BlockData getBlockData(int x, int y, int z) {
        return getBlock(x, y, z).getBlockData();
    }
}
