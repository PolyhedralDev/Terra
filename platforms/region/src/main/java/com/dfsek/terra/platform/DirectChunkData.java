package com.dfsek.terra.platform;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.generator.ChunkGenerator;
import net.querz.mca.Chunk;
import net.querz.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class DirectChunkData implements ChunkGenerator.ChunkData {
    private final Chunk delegate;
    private final int offX;
    private final int offZ;

    public DirectChunkData(Chunk delegate, int offX, int offZ) {
        this.delegate = delegate;
        this.offX = offX;
        this.offZ = offZ;
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
}
