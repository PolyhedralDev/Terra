package com.dfsek.terra.api.gaea.tree.fractal.trees;

import com.dfsek.terra.api.gaea.math.ProbabilityCollection;
import com.dfsek.terra.api.gaea.tree.fractal.FractalTree;
import com.dfsek.terra.api.gaea.tree.fractal.TreeGeometry;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.world.WorldHandle;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.vector.Location;
import com.dfsek.terra.api.generic.world.vector.Vector3;
import com.dfsek.terra.util.MaterialSet;

import java.util.Random;


public class IceSpike extends FractalTree {
    private final TreeGeometry geo;
    private final ProbabilityCollection<BlockData> ice;

    @Override
    public MaterialSet getSpawnable() {
        return MaterialSet.get(main.getWorldHandle().createMaterialData("minecraft:stone"),
                main.getWorldHandle().createMaterialData("minecraft:gravel"),
                main.getWorldHandle().createMaterialData("minecraft:snow_block"),
                main.getWorldHandle().createMaterialData("minecraft:grass_block"));
    }

    /**
     * Instantiates a TreeGrower at an origin location.
     *
     * @param origin - The origin location.
     * @param random - The random object to use whilst generating the tree.
     */
    public IceSpike(Location origin, Random random, TerraPlugin main) {
        super(origin, random, main);
        geo = new TreeGeometry(this);
        WorldHandle handle = main.getWorldHandle();
        ice = new ProbabilityCollection<BlockData>().add(handle.createBlockData("minecraft:packed_ice"), 95).add(handle.createBlockData("minecraft:blue_ice"), 5);
    }

    private double getOffset() {
        return (getRandom().nextDouble() - 0.5D);
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    @Override
    public void grow() {
        Vector3 direction = new Vector3(getOffset(), 0, getOffset());
        Location l1 = super.getOrigin().clone();
        int h = super.getRandom().nextInt(16) + 8;
        for(int i = 0; i < h; i++) {
            geo.generateSponge(l1.clone().add(0, i, 0).add(direction.clone().multiply(i)), ice, (int) ((1 - ((double) i / h)) * 2 + 1), true, 80);
        }
        for(int i = 0; i < h/3; i++) {
            setBlock(l1.clone().add(0, h + i, 0).add(direction.clone().multiply(h + i)), ice.get(super.getRandom()));
        }
    }
}
