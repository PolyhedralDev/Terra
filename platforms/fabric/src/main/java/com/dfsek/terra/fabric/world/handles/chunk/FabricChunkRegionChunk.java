package com.dfsek.terra.fabric.world.handles.chunk;

import com.dfsek.terra.api.generic.world.Chunk;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.fabric.world.FabricBlock;
import com.dfsek.terra.fabric.world.handles.world.FabricWorldAccess;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;

public class FabricChunkRegionChunk implements Chunk {
    private final ChunkRegion chunkRegion;
    private final int x;
    private final int z;

    public FabricChunkRegionChunk(ChunkRegion chunkRegion) {
        this.chunkRegion = chunkRegion;
        this.x = chunkRegion.getCenterChunkX() << 4;
        this.z = chunkRegion.getCenterChunkZ() << 4;
    }

    @Override
    public int getX() {
        return chunkRegion.getCenterChunkX();
    }

    @Override
    public int getZ() {
        return chunkRegion.getCenterChunkZ();
    }

    @Override
    public World getWorld() {
        return new FabricWorldAccess(chunkRegion);
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        BlockPos pos = new BlockPos(x + this.x, y, z + this.z);
        return new FabricBlock(pos, chunkRegion);
    }

    @Override
    public ChunkRegion getHandle() {
        return chunkRegion;
    }
}
