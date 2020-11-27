package com.dfsek.terra.generation.items;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.world.Flora;

import java.util.List;

public class TerraFlora implements Flora {
    @Override
    public List<Block> getValidSpawnsAt(Chunk chunk, int i, int i1, Range range) {
        return null;
    }

    @Override
    public boolean plant(Location location) {
        return false;
    }
}
