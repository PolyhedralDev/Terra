package com.dfsek.terra.minestom.chunk;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.world.chunk.generation.ProtoChunk;

import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.UnitModifier;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MinestomProtoChunk implements ProtoChunk {
    private static final Logger log = LoggerFactory.getLogger(MinestomProtoChunk.class);
    private final int minHeight;
    private final int maxHeight;
    private final UnitModifier modifier;

    public MinestomProtoChunk(int maxHeight, int minHeight, @NotNull UnitModifier modifier) {
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.modifier = modifier;
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockState blockState) {
        try {
            modifier.setRelative(x, y - minHeight, z, (Block) blockState.getHandle());
        } catch(Exception e) {
            log.error("Failed setting Block at {} {} {} to {} (min={}, max={})", x, y, z, blockState.getHandle(), minHeight, maxHeight);
        }
    }

    @Override
    public @NotNull BlockState getBlock(int x, int y, int z) {
        System.out.println("Block access at " + x + ", " + y + ", " + z + " is not supported.");
        return null;
    }

    @Override
    public Object getHandle() {
        return modifier;
    }
}
