package com.dfsek.terra.api.gaea.tree.fractal.trees;

import com.dfsek.terra.api.gaea.tree.fractal.FractalTree;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.vector.Location;
import com.dfsek.terra.util.MaterialSet;

import java.util.Random;

public class ShatteredPillar extends FractalTree {

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
    public ShatteredPillar(Location origin, Random random, TerraPlugin main) {
        super(origin, random, main);
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    @Override
    public void grow() {
        BlockData obsidian = getMain().getWorldHandle().createBlockData("minecraft:obsidian");
        int h = super.getRandom().nextInt(5) + 8;
        int max = h;
        for(int i = - h; i < h; i++) setBlock(super.getOrigin().clone().add(0, i, 0), obsidian);
        h = h + (getRandom().nextBoolean() ? getRandom().nextInt(3) + 1 : - (getRandom().nextInt(3) + 1));
        int[] crystalLoc = new int[] {0, 0};
        if(h > max) {
            max = h;
            crystalLoc = new int[] {1, 0};
        }
        for(int i = - h; i < h; i++) setBlock(super.getOrigin().clone().add(1, i, 0), obsidian);
        h = h + (getRandom().nextBoolean() ? getRandom().nextInt(3) + 1 : - (getRandom().nextInt(3) + 1));
        if(h > max) {
            max = h;
            crystalLoc = new int[] {0, 1};
        }
        for(int i = - h; i < h; i++) setBlock(super.getOrigin().clone().add(0, i, 1), obsidian);
        h = h + (getRandom().nextBoolean() ? getRandom().nextInt(3) + 1 : - (getRandom().nextInt(3) + 1));
        if(h > max) {
            max = h;
            crystalLoc = new int[] {1, 1};
        }
        for(int i = - h; i < h; i++) setBlock(super.getOrigin().clone().add(1, i, 1), obsidian);
    }
}
