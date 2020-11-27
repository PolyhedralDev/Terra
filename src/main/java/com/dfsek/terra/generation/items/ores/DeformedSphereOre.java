package com.dfsek.terra.generation.items.ores;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.math.Range;

import java.util.Random;

public class DeformedSphereOre extends Ore {
    public DeformedSphereOre(Range height, Range amount, BlockData material) {
        super(height, amount, material);
    }


    public void generate(Location origin, Chunk c, Random r) {
        // TODO: implementation
    }
}
