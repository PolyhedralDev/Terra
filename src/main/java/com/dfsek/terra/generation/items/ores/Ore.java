package com.dfsek.terra.generation.items.ores;

import com.dfsek.terra.util.MaterialSet;
import org.bukkit.Chunk;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import java.util.Random;

public abstract class Ore {

    private final BlockData material;
    private final MaterialSet replaceable;
    private final boolean applyGravity;

    public Ore(BlockData material, MaterialSet replaceable, boolean applyGravity) {

        this.material = material;
        this.replaceable = replaceable;
        this.applyGravity = applyGravity;
    }

    public abstract void generate(Vector origin, Chunk c, Random r);

    public BlockData getMaterial() {
        return material;
    }

    public MaterialSet getReplaceable() {
        return replaceable;
    }

    public boolean isApplyGravity() {
        return applyGravity;
    }

    public enum Type {
        VANILLA, SPHERE
    }
}
