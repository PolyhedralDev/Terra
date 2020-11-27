package com.dfsek.terra.generation.items.ores;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.math.Range;

import java.util.Random;
import java.util.Set;

public abstract class Ore {
    private final Range height;
    private final Range amount;
    private final BlockData material;
    private final Set<Material> replaceable;
    private final boolean applyGravity;

    public Ore(Range height, Range amount, BlockData material, Set<Material> replaceable, boolean applyGravity) {
        this.height = height;
        this.amount = amount;
        this.material = material;
        this.replaceable = replaceable;
        this.applyGravity = applyGravity;
    }

    public abstract void generate(Location origin, Chunk c, Random r);

    public Range getHeight() {
        return height;
    }

    public Range getAmount() {
        return amount;
    }

    public BlockData getMaterial() {
        return material;
    }

    public Set<Material> getReplaceable() {
        return replaceable;
    }

    public boolean isApplyGravity() {
        return applyGravity;
    }
}
