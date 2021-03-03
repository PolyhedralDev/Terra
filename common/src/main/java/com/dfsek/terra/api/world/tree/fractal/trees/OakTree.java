package com.dfsek.terra.api.world.tree.fractal.trees;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.api.world.tree.fractal.FractalTree;
import com.dfsek.terra.api.world.tree.fractal.TreeGeometry;
import net.jafama.FastMath;

import java.util.Random;


public class OakTree extends FractalTree {
    private final TreeGeometry geo;

    @Override
    public MaterialSet getSpawnable() {
        return MaterialSet.get(main.getWorldHandle().createBlockData("minecraft:podzol"),
                main.getWorldHandle().createBlockData("minecraft:grass_block"));
    }

    /**
     * Instantiates a TreeGrower at an origin location.
     */
    public OakTree(TerraPlugin main) {
        super(main);
        geo = new TreeGeometry(this);
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    @Override
    public void grow(Location origin, Random random) {
        growBranch(origin.clone(), new Vector3(random.nextInt(5) - 2, random.nextInt(4) + 6, random.nextInt(5) - 2), 2, 0, random);
    }

    private void growBranch(Location l1, Vector3 diff, double d1, int recursions, Random r) {
        BlockData wood = getMain().getWorldHandle().createBlockData("minecraft:oak_wood");
        BlockData leaves = getMain().getWorldHandle().createBlockData("minecraft:oak_leaves");
        if(recursions > 1) {
            geo.generateSphere(l1, leaves, 1 + r.nextInt(2) + (3 - recursions), false, r);
            if(recursions > 2) return;
        }
        if(diff.getY() < 0) diff.rotateAroundAxis(TreeGeometry.getPerpendicular(diff.clone()).normalize(), FastMath.PI);
        int d = (int) diff.length();
        for(int i = 0; i < d; i++) {
            geo.generateSphere(l1.clone().add(diff.clone().multiply((double) i / d)), wood, FastMath.max((int) d1, 0), true, r);
        }
        double runningAngle = (double) 45 / (recursions + 1);
        growBranch(l1.clone().add(diff), diff.clone().multiply(0.75).rotateAroundX(FastMath.toRadians(runningAngle + getNoise(r))).rotateAroundZ(FastMath.toRadians(getNoise(r))),
                d1 - 1, recursions + 1, r);
        growBranch(l1.clone().add(diff), diff.clone().multiply(0.75).rotateAroundX(FastMath.toRadians(-runningAngle + getNoise(r))).rotateAroundZ(FastMath.toRadians(getNoise(r))),
                d1 - 1, recursions + 1, r);
        growBranch(l1.clone().add(diff), diff.clone().multiply(0.75).rotateAroundZ(FastMath.toRadians(runningAngle + getNoise(r))).rotateAroundX(FastMath.toRadians(getNoise(r))),
                d1 - 1, recursions + 1, r);
        growBranch(l1.clone().add(diff), diff.clone().multiply(0.75).rotateAroundZ(FastMath.toRadians(-runningAngle + getNoise(r))).rotateAroundX(FastMath.toRadians(getNoise(r))),
                d1 - 1, recursions + 1, r);
    }

    private int getNoise(Random r) {
        return r.nextInt(60) - 30;
    }
}
