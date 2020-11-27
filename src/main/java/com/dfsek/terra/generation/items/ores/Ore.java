package com.dfsek.terra.generation.items.ores;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.math.Range;

import java.util.Random;

public abstract class Ore {
    private final Range height;
    private final Range amount;
    private final BlockData material;

    public Ore(Range height, Range amount, BlockData material) {
        this.height = height;
        this.amount = amount;
        this.material = material;
    }

    public BlockData getMaterial() {
        return material;
    }

    public Range getAmount() {
        return amount;
    }

    public Range getHeight() {
        return height;
    }

    public abstract void generate(Location origin, Chunk c, Random r);
}
