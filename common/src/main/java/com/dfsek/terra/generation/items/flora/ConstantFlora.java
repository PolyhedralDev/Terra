package com.dfsek.terra.generation.items.flora;

import com.dfsek.terra.api.gaea.math.Range;
import com.dfsek.terra.api.gaea.util.GlueList;
import com.dfsek.terra.api.gaea.world.Flora;
import com.dfsek.terra.api.generic.world.Chunk;
import com.dfsek.terra.api.generic.world.block.Block;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.block.BlockFace;
import com.dfsek.terra.api.generic.world.vector.Location;
import com.dfsek.terra.util.MaterialSet;

import java.util.List;

public class ConstantFlora implements Flora {
    private final List<BlockData> data;

    private final MaterialSet spawns;

    public ConstantFlora(MaterialSet spawns, List<BlockData> data) {
        this.data = data;
        this.spawns = spawns;
    }

    @Override
    public List<Block> getValidSpawnsAt(Chunk chunk, int x, int z, Range check) {
        List<Block> blocks = new GlueList<>();
        for(int y : check) {
            Block block = chunk.getBlock(x, y, z);
            if(spawns.contains(block.getType()) && valid(block)) {
                blocks.add(chunk.getBlock(x, y, z));
            }
        }
        return blocks;
    }

    private boolean valid(Block block) {
        for(int i = 1; i < data.size() + 1; i++) {
            block = block.getRelative(BlockFace.UP);
            if(!block.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public boolean plant(Location l) {
        for(int i = 1; i < data.size() + 1; i++) {
            l.clone().add(0, i, 0).getBlock().setBlockData(data.get(i - 1), false);
        }
        return true;
    }
}
