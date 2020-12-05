package com.dfsek.terra.generation.items.flora;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.util.GlueList;
import org.polydev.gaea.world.Flora;

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
        location.add(0, 1, 0).getBlock().setBlockData(data);
        return true;
    }
}
