package com.dfsek.terra.minestom.world;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.generator.ChunkData;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MinestomChunkData implements ChunkData {
    private static final BlockData AIR = new MinestomBlockData(Block.AIR);
    private final ChunkBatch batch;
    private final Map<Long, MinestomBlockData> dataMap = new Long2ObjectOpenHashMap<>();

    public MinestomChunkData(ChunkBatch batch) {
        this.batch = batch;
    }

    @Override
    public Object getHandle() {
        return batch;
    }

    @Override
    public void setBlock(int x, int y, int z, @NotNull BlockData blockData) {
        MinestomBlockData d = ((MinestomBlockData) blockData);
        dataMap.put(((long) y << 12) + ((long) x << 4) + y, d);
        batch.setBlock(x, y, z, d.getHandle());
    }

    @Override
    public @NotNull BlockData getBlockData(int x, int y, int z) {
        BlockData data = dataMap.get(((long) y << 12) + ((long) x << 4) + y);
        return data == null ? AIR : data;
    }

    @Override
    public int getMaxHeight() {
        return 255;
    }
}
