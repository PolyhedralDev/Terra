package com.dfsek.terra.api.world.tree.fractal.trees;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.api.world.tree.fractal.FractalTree;
import com.dfsek.terra.api.world.tree.fractal.TreeGeometry;

import java.util.Random;


public class IceSpike extends FractalTree {
    private final TreeGeometry geo;
    private final ProbabilityCollection<BlockData> ice;

    @Override
    public MaterialSet getSpawnable() {
        return MaterialSet.get(main.getWorldHandle().createBlockData("minecraft:stone"),
                main.getWorldHandle().createBlockData("minecraft:gravel"),
                main.getWorldHandle().createBlockData("minecraft:snow_block"),
                main.getWorldHandle().createBlockData("minecraft:grass_block"));
    }

    /**
     * Instantiates a TreeGrower at an origin location.
     */
    public IceSpike(TerraPlugin main) {
        super(main);
        geo = new TreeGeometry(this);
        WorldHandle handle = main.getWorldHandle();
        ice = new ProbabilityCollection<BlockData>().add(handle.createBlockData("minecraft:packed_ice"), 95).add(handle.createBlockData("minecraft:blue_ice"), 5);
    }

    private double getOffset(Random r) {
        return (r.nextDouble() - 0.5D);
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    @Override
    public void grow(Location origin, Random random) {
        Vector3 direction = new Vector3(getOffset(random), 0, getOffset(random));
        Location l1 = origin.clone();
        int h = random.nextInt(16) + 8;
        for(int i = 0; i < h; i++) {
            geo.generateSponge(l1.clone().add(0, i, 0).add(direction.clone().multiply(i)), ice, (int) ((1 - ((double) i / h)) * 2 + 1), true, 80, random);
        }
        for(int i = 0; i < h / 3; i++) {
            setBlock(l1.clone().add(0, h + i, 0).add(direction.clone().multiply(h + i)), ice.get(random));
        }
    }
}
