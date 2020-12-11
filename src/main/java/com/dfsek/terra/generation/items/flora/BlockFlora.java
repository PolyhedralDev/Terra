package com.dfsek.terra.generation.items.flora;

import com.dfsek.terra.api.gaea.math.Range;
import com.dfsek.terra.api.gaea.util.GlueList;
import com.dfsek.terra.api.gaea.world.Flora;
import com.dfsek.terra.api.generic.world.Chunk;
import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.BlockFace;
import com.dfsek.terra.api.generic.world.vector.Location;

import java.util.List;

/**
 * Flora that is just 1 layer of a single block.
 */
public class BlockFlora implements Flora {

    private final BlockData data;

    public BlockFlora(BlockData data) {
        this.data = data;
    }

    @Override
    public List<Block> getValidSpawnsAt(Chunk chunk, int x, int z, Range range) {
        Block current = chunk.getBlock(x, range.getMin(), z);
        List<Block> blocks = new GlueList<>();
        for(int y : range) {
            if(y > 255 || y < 0) continue;
            current = current.getRelative(BlockFace.UP);
            if(current.getType().isSolid() && current.getRelative(BlockFace.UP).isEmpty()) {
                blocks.add(current); // Add all blocks that are solid with air directly above.
            }
        }
        return blocks;
    }

    @Override
    public boolean plant(Location location) {
        location.add(0, 1, 0).getBlock().setBlockData(data, true);
        return true;
    }
}
