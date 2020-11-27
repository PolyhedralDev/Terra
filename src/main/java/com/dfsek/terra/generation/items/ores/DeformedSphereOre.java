package com.dfsek.terra.generation.items.ores;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.math.Range;

import java.util.Random;
import java.util.Set;

public class DeformedSphereOre extends Ore {
    public DeformedSphereOre(Range height, Range amount, BlockData material, Set<Material> replaceable, boolean applyGravity) {
        super(height, amount, material, replaceable, applyGravity);
    }


    public void generate(Location origin, Chunk c, Random r) {
        // TODO: implementation
    }
}
