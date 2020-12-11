package com.dfsek.terra.api.gaea.tree.fractal.trees;

import com.dfsek.terra.api.gaea.tree.fractal.FractalTree;
import com.dfsek.terra.api.generic.world.vector.Location;
import org.bukkit.Material;

import java.util.Random;

public class SmallShatteredPillar extends FractalTree {
    /**
     * Instantiates a TreeGrower at an origin location.
     *
     * @param origin - The origin location.
     * @param random - The random object to use whilst generating the tree.
     */
    public SmallShatteredPillar(Location origin, Random random) {
        super(origin, random);
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    @Override
    public void grow() {
        int h = super.getRandom().nextInt(5) + 5;
        for(int i = - h; i < h; i++) setBlock(super.getOrigin().clone().add(0, i, 0), Material.OBSIDIAN);
    }
}
