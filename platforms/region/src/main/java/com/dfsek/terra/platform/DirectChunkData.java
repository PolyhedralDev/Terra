package com.dfsek.terra.platform;

import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.platform.world.generator.ChunkGenerator;
import net.querz.mca.Chunk;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class DirectChunkData implements ChunkGenerator.ChunkData, com.dfsek.terra.api.platform.world.Chunk {
    private final Chunk delegate;
    private final DirectWorld world;
    private final int x;
    private final int z;

    public DirectChunkData(Chunk delegate, DirectWorld world, int x, int z) {
        this.delegate = delegate;
        this.world = world;
        this.x = x;
        this.z = z;
    }

    @Override
    public Object getHandle() {
        return delegate;
    }

    @Override
    public int getMaxHeight() {
        return 255;
    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockData blockData) {
        delegate.setBlockStateAt(x, y, z, ((Data) blockData).getHandle(), false);
    }

    @Override
    public @NotNull BlockData getBlockData(int x, int y, int z) {
        CompoundTag tag = delegate.getBlockStateAt(x, y, z);
        if(tag == null) return new Data("minecraft:air");
        return new Data(tag.getString("Name"));
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return new DirectBlock(world, new Vector3(x + (this.x << 4), y, z + (this.z << 4)));
    }
}
