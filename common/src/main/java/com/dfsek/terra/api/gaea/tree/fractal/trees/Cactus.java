package com.dfsek.terra.api.gaea.tree.fractal.trees;

import com.dfsek.terra.api.gaea.tree.fractal.FractalTree;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.vector.Location;

import java.util.Random;

public class Cactus extends FractalTree {
    /**
     * Instantiates a TreeGrower at an origin location.
     *
     * @param origin - The origin location.
     * @param random - The random object to use whilst generating the tree.
     */
    public Cactus(Location origin, Random random, TerraPlugin main) {
        super(origin, random, main);
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    @Override
    public void grow() {
        BlockData cactus = getMain().getWorldHandle().createBlockData("minecraft:cactus");
        int h = super.getRandom().nextInt(4) + 1;
        for(int i = 0; i < h; i++) setBlock(super.getOrigin().clone().add(0, i, 0), cactus);
    }
}
