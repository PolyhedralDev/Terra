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
import net.jafama.FastMath;

import java.util.Random;

public class ShatteredTree extends FractalTree {
    private final TreeGeometry geo;
    private final ProbabilityCollection<BlockData> bark;
    private final ProbabilityCollection<BlockData> leaves;

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
    public ShatteredTree(Location origin, Random random, TerraPlugin main) {
        super(origin, random, main);
        geo = new TreeGeometry(this);
        WorldHandle handle = main.getWorldHandle();

        bark = new ProbabilityCollection<BlockData>()
                .add(handle.createBlockData("minecraft:obsidian"), 1)
                .add(handle.createBlockData("minecraft:black_concrete"), 1);
        leaves = new ProbabilityCollection<BlockData>()
                .add(handle.createBlockData("minecraft:purple_stained_glass"), 1)
                .add(handle.createBlockData("minecraft:magenta_stained_glass"), 1);
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    @Override
    public void grow() {
        growBranch(super.getOrigin().clone(), new Vector3(super.getRandom().nextInt(5) - 2, super.getRandom().nextInt(4) + 6, super.getRandom().nextInt(5) - 2), 1, 0);

    }

    private void growBranch(Location l1, Vector3 diff, double d1, int recursions) {
        if(recursions > 2) {
            geo.generateSphere(l1, leaves, 1 + super.getRandom().nextInt(2), false);
            return;
        }
        if(diff.getY() < 0) diff.rotateAroundAxis(TreeGeometry.getPerpendicular(diff.clone()).normalize(), FastMath.PI);
        int d = (int) diff.length();
        for(int i = 0; i < d; i++) {
            geo.generateSphere(l1.clone().add(diff.clone().multiply((double) i / d)), bark, FastMath.max((int) d1, 0), true);
        }
        double runningAngle = (double) 45 / (recursions + 1);
        growBranch(l1.clone().add(diff), diff.clone().multiply(0.9).rotateAroundX(FastMath.toRadians(runningAngle + getNoise())).rotateAroundZ(FastMath.toRadians(getNoise())),
                d1 - 1, recursions + 1);
        growBranch(l1.clone().add(diff), diff.clone().multiply(0.9).rotateAroundX(FastMath.toRadians(- runningAngle + getNoise())).rotateAroundZ(FastMath.toRadians(getNoise())),
                d1 - 1, recursions + 1);
        growBranch(l1.clone().add(diff), diff.clone().multiply(0.9).rotateAroundZ(FastMath.toRadians(runningAngle + getNoise())).rotateAroundX(FastMath.toRadians(getNoise())),
                d1 - 1, recursions + 1);
        growBranch(l1.clone().add(diff), diff.clone().multiply(0.9).rotateAroundZ(FastMath.toRadians(- runningAngle + getNoise())).rotateAroundX(FastMath.toRadians(getNoise())),
                d1 - 1, recursions + 1);
    }

    private int getNoise() {
        return super.getRandom().nextInt(90) - 45;
    }
}
