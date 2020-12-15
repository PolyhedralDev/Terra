package com.dfsek.terra.api.gaea.tree.fractal.trees;

import com.dfsek.terra.api.gaea.tree.fractal.FractalTree;
import com.dfsek.terra.api.gaea.tree.fractal.TreeGeometry;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.world.block.BlockData;
import com.dfsek.terra.api.generic.world.vector.Location;
import com.dfsek.terra.api.generic.world.vector.Vector3;
import com.dfsek.terra.util.MaterialSet;
import net.jafama.FastMath;

import java.util.Random;


public class OakTree extends FractalTree {
    private final TreeGeometry geo;

    @Override
    public MaterialSet getSpawnable() {
        return MaterialSet.get(main.getWorldHandle().createMaterialData("minecraft:podzol"),
                main.getWorldHandle().createMaterialData("minecraft:grass_block"));
    }

    /**
     * Instantiates a TreeGrower at an origin location.
     *
     * @param origin - The origin location.
     * @param random - The random object to use whilst generating the tree.
     */
    public OakTree(Location origin, Random random, TerraPlugin main) {
        super(origin, random, main);
        geo = new TreeGeometry(this);
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    @Override
    public void grow() {
        growBranch(super.getOrigin().clone(), new Vector3(super.getRandom().nextInt(5) - 2, super.getRandom().nextInt(4) + 6, super.getRandom().nextInt(5) - 2), 2, 0);
    }

    private void growBranch(Location l1, Vector3 diff, double d1, int recursions) {
        BlockData wood = getMain().getWorldHandle().createBlockData("minecraft:oak_wood");
        BlockData leaves = getMain().getWorldHandle().createBlockData("minecraft:oak_leave");
        if(recursions > 1) {
            geo.generateSphere(l1, leaves, 1 + super.getRandom().nextInt(2) + (3 - recursions), false);
            if(recursions > 2) return;
        }
        if(diff.getY() < 0) diff.rotateAroundAxis(TreeGeometry.getPerpendicular(diff.clone()).normalize(), FastMath.PI);
        int d = (int) diff.length();
        for(int i = 0; i < d; i++) {
            geo.generateSphere(l1.clone().add(diff.clone().multiply((double) i / d)), wood, FastMath.max((int) d1, 0), true);
        }
        double runningAngle = (double) 45 / (recursions + 1);
        growBranch(l1.clone().add(diff), diff.clone().multiply(0.75).rotateAroundX(FastMath.toRadians(runningAngle + getNoise())).rotateAroundZ(FastMath.toRadians(getNoise())),
                d1 - 1, recursions + 1);
        growBranch(l1.clone().add(diff), diff.clone().multiply(0.75).rotateAroundX(FastMath.toRadians(- runningAngle + getNoise())).rotateAroundZ(FastMath.toRadians(getNoise())),
                d1 - 1, recursions + 1);
        growBranch(l1.clone().add(diff), diff.clone().multiply(0.75).rotateAroundZ(FastMath.toRadians(runningAngle + getNoise())).rotateAroundX(FastMath.toRadians(getNoise())),
                d1 - 1, recursions + 1);
        growBranch(l1.clone().add(diff), diff.clone().multiply(0.75).rotateAroundZ(FastMath.toRadians(- runningAngle + getNoise())).rotateAroundX(FastMath.toRadians(getNoise())),
                d1 - 1, recursions + 1);
    }

    private int getNoise() {
        return super.getRandom().nextInt(60) - 30;
    }
}
