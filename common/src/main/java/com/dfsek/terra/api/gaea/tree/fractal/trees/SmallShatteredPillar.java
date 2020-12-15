package com.dfsek.terra.api.gaea.tree.fractal.trees;

import com.dfsek.terra.api.gaea.tree.fractal.FractalTree;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.vector.Location;
import com.dfsek.terra.util.MaterialSet;

import java.util.Random;

public class SmallShatteredPillar extends FractalTree {

    @Override
    public MaterialSet getSpawnable() {
        return MaterialSet.get(main.getWorldHandle().createMaterialData("minecraft:end_stone"));
    }

    /**
     * Instantiates a TreeGrower at an origin location.
     *
     * @param origin - The origin location.
     * @param random - The random object to use whilst generating the tree.
     */
    public SmallShatteredPillar(Location origin, Random random, TerraPlugin main) {
        super(origin, random, main);
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    @Override
    public void grow() {
        int h = super.getRandom().nextInt(5) + 5;
        BlockData obsidian = getMain().getWorldHandle().createBlockData("minecraft:obsidian");
        for(int i = - h; i < h; i++) setBlock(super.getOrigin().clone().add(0, i, 0), obsidian);
    }
}
