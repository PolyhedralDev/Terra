package com.dfsek.terra.platform;

import com.dfsek.terra.api.block.BlockState;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.generator.ChunkData;
import net.querz.mca.Chunk;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class DirectChunkData implements ChunkData, com.dfsek.terra.api.world.Chunk {
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
    public void setBlock(int x, int y, int z, @NotNull BlockState blockState) {
        delegate.setBlockStateAt(x, y, z, ((State) blockState).getHandle(), false);
    }

    @Override
    public @NotNull BlockState getBlock(int x, int y, int z) {
        CompoundTag tag = delegate.getBlockStateAt(x, y, z);
        if(tag == null) return new State("minecraft:air");
        return new State(tag.getString("Name"));
    }

    @Override
    public void setBlock(int x, int y, int z, BlockState data, boolean physics) {
        setBlock(x, y, z, data);
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

}
