package com.dfsek.terra.api.world.tree.fractal;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.entity.Entity;
import com.dfsek.terra.api.platform.entity.EntityType;
import com.dfsek.terra.util.MaterialSet;

import java.util.Random;
import java.util.function.Consumer;


public abstract class FractalTree {
    protected final TerraPlugin main;

    public abstract MaterialSet getSpawnable();

    /**
     * Instantiates a TreeGrower at an origin location.
     */
    public FractalTree(TerraPlugin plugin) {
        this.main = plugin;
    }

    /**
     * Sets a block in the tree's storage map to a material.
     *
     * @param l - The location to set.
     * @param m - The material to which it will be set.
     */
    public void setBlock(Location l, BlockData m) {
        l.getBlock().setBlockData(m, false);
    }

    public TerraPlugin getMain() {
        return main;
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    public abstract void grow(Location origin, Random random);

    public void spawnEntity(Location spawn, EntityType type, Consumer<Entity> consumer) {
        consumer.accept(spawn.getWorld().spawnEntity(spawn, type));
    }
}
