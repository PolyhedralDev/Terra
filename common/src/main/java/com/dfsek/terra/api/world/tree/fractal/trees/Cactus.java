package com.dfsek.terra.api.world.tree.fractal.trees;

import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.world.tree.fractal.FractalTree;
import com.dfsek.terra.util.MaterialSet;

import java.util.Random;

public class Cactus extends FractalTree {
    @Override
    public MaterialSet getSpawnable() {
        return MaterialSet.get(main.getWorldHandle().createMaterialData("minecraft:sand"),
                main.getWorldHandle().createMaterialData("minecraft:red_sand"));
    }


    /**
     * Instantiates a TreeGrower at an origin location.
     */
    public Cactus(TerraPlugin main) {
        super(main);
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    @Override
    public void grow(Location origin, Random random) {
        BlockData cactus = getMain().getWorldHandle().createBlockData("minecraft:cactus");
        int h = random.nextInt(4) + 1;
        for(int i = 0; i < h; i++) setBlock(origin.clone().add(0, i, 0), cactus);
    }
}
