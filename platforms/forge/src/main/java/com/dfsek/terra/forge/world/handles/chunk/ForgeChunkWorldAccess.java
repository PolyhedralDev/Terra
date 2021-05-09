package com.dfsek.terra.forge.world.handles.chunk;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.forge.world.block.ForgeBlock;
import com.dfsek.terra.forge.world.block.ForgeBlockData;
import com.dfsek.terra.forge.world.handles.world.ForgeWorldAccess;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.jetbrains.annotations.NotNull;

public class ForgeChunkWorldAccess implements Chunk {
    private final IWorld chunkRegion;
    private final int x;
    private final int z;

    public ForgeChunkWorldAccess(IWorld chunkRegion, int x, int z) {
        this.chunkRegion = chunkRegion;
        this.x = x << 4;
        this.z = z << 4;
    }

    @Override
    public int getX() {
        return x >> 4;
    }

    @Override
    public int getZ() {
        return z >> 4;
    }

    @Override
    public World getWorld() {
        return new ForgeWorldAccess(chunkRegion);
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        BlockPos pos = new BlockPos(x + this.x, y, z + this.z);
        return new ForgeBlock(pos, chunkRegion);
    }

    @Override
    public IWorld getHandle() {
        return chunkRegion;
    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockData blockData) {
        chunkRegion.setBlock(new BlockPos(x + this.x, y, z + this.z), ((ForgeBlockData) blockData).getHandle(), 0);
    }

    @Override
    public @NotNull BlockData getBlockData(int x, int y, int z) {
        return getBlock(x, y, z).getBlockData();
    }
}
