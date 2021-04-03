package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.util.generic.either.Either;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.utils.BlockPosition;
import org.jetbrains.annotations.NotNull;

public class MinestomChunk implements Chunk, MinestomChunkAccess {
    private final net.minestom.server.instance.Chunk chunk;
    private final ChunkBatch batch;
    private final MinestomWorld world;

    public MinestomChunk(net.minestom.server.instance.Chunk chunk, ChunkBatch batch, MinestomWorld world) {
        this.chunk = chunk;
        this.batch = batch;
        this.world = world;
    }

    @Override
    public ChunkBatch getHandle() {
        return batch;
    }

    @Override
    public int getX() {
        return chunk.getChunkX();
    }

    @Override
    public int getZ() {
        return chunk.getChunkZ();
    }

    @Override
    public World getWorld() {
        return new MinestomChunkWorld(batch, chunk, world.getHandle());
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return new MinestomBlock(net.minestom.server.instance.block.Block.fromStateId(chunk.getBlockStateId(x, y, z)), new BlockPosition((getX() << 4) + x, y, (getZ() << 4) + z), Either.left(this));
    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockData blockData) {
        batch.setBlock(x, y, z, ((MinestomBlockData) blockData).getHandle());
    }

    @Override
    public @NotNull BlockData getBlockData(int x, int y, int z) {
        return getBlock(x, y, z).getBlockData();
    }
}
