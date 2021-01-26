package com.dfsek.terra.api.world.tree.fractal.trees;

import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.world.tree.fractal.FractalTree;
import com.dfsek.terra.util.MaterialSet;

import java.util.Random;

public class ShatteredPillar extends FractalTree {

    @Override
    public MaterialSet getSpawnable() {
        return MaterialSet.get(main.getWorldHandle().createMaterialData("minecraft:end_stone"));
    }

    /**
     * Instantiates a TreeGrower at an origin location.
     */
    public ShatteredPillar(TerraPlugin main) {
        super(main);
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     * @param origin
     * @param random
     */
    @Override
    public void grow(Location origin, Random random) {
        BlockData obsidian = getMain().getWorldHandle().createBlockData("minecraft:obsidian");
        int h = random.nextInt(5) + 8;
        int max = h;
        for(int i = -h; i < h; i++) setBlock(origin.clone().add(0, i, 0), obsidian);
        h = h + (random.nextBoolean() ? random.nextInt(3) + 1 : -(random.nextInt(3) + 1));
        int[] crystalLoc = new int[] {0, 0};
        if(h > max) {
            max = h;
            crystalLoc = new int[] {1, 0};
        }
        for(int i = -h; i < h; i++) setBlock(origin.clone().add(1, i, 0), obsidian);
        h = h + (random.nextBoolean() ? random.nextInt(3) + 1 : -(random.nextInt(3) + 1));
        if(h > max) {
            max = h;
            crystalLoc = new int[] {0, 1};
        }
        for(int i = -h; i < h; i++) setBlock(origin.clone().add(0, i, 1), obsidian);
        h = h + (random.nextBoolean() ? random.nextInt(3) + 1 : -(random.nextInt(3) + 1));
        if(h > max) {
            max = h;
            crystalLoc = new int[] {1, 1};
        }
        for(int i = -h; i < h; i++) setBlock(origin.clone().add(1, i, 1), obsidian);
    }
}
