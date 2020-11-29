package com.dfsek.terra.generation.items.ores;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.Set;

public abstract class Ore {

    private final BlockData material;
    private final Set<Material> replaceable;
    private final boolean applyGravity;

    public Ore(BlockData material, Set<Material> replaceable, boolean applyGravity) {

        this.material = material;
        this.replaceable = replaceable;
        this.applyGravity = applyGravity;
    }

    public abstract void generate(Vector origin, Chunk c, Random r);

    public BlockData getMaterial() {
        return material;
    }

    public Set<Material> getReplaceable() {
        return replaceable;
    }

    public boolean isApplyGravity() {
        return applyGravity;
    }

    public enum Type {
        VANILLA, SPHERE
    }
}
