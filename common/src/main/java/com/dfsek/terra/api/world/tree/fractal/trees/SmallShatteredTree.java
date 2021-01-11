package com.dfsek.terra.api.world.tree.fractal.trees;

import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.world.tree.fractal.FractalTree;
import com.dfsek.terra.api.world.tree.fractal.TreeGeometry;
import com.dfsek.terra.util.MaterialSet;
import net.jafama.FastMath;

import java.util.Random;

public class SmallShatteredTree extends FractalTree {
    private final TreeGeometry geo;
    private final ProbabilityCollection<BlockData> bark;
    private final ProbabilityCollection<BlockData> leaves;

    @Override
    public MaterialSet getSpawnable() {
        return MaterialSet.get(main.getWorldHandle().createMaterialData("minecraft:end_stone"));
    }

    /**
     * Instantiates a TreeGrower at an origin location.
     */
    public SmallShatteredTree(TerraPlugin main) {
        super(main);
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
    public void grow(Location origin, Random random) {
        growBranch(origin.clone(), new Vector3(random.nextInt(5) - 2, random.nextInt(3) + 4, random.nextInt(5) - 2), 1.5, 0, random);

    }

    private void growBranch(Location l1, Vector3 diff, double d1, int recursions, Random r) {
        if(recursions > 2) {
            geo.generateSphere(l1, leaves, 1 + r.nextInt(2) + (3 - recursions), false, r);
            return;
        }
        if(diff.getY() < 0) diff.rotateAroundAxis(TreeGeometry.getPerpendicular(diff.clone()).normalize(), FastMath.PI);
        int d = (int) diff.length();
        for(int i = 0; i < d; i++) {
            geo.generateSphere(l1.clone().add(diff.clone().multiply((double) i / d)), bark, FastMath.max((int) d1, 0), true, r);
        }
        double runningAngle = (double) 45 / (recursions + 1);
        growBranch(l1.clone().add(diff), diff.clone().multiply(0.7).rotateAroundX(FastMath.toRadians(runningAngle + getNoise(r))).rotateAroundZ(FastMath.toRadians(getNoise(r))),
                d1 - 1, recursions + 1, r);
        growBranch(l1.clone().add(diff), diff.clone().multiply(0.7).rotateAroundX(FastMath.toRadians(-runningAngle + getNoise(r))).rotateAroundZ(FastMath.toRadians(getNoise(r))),
                d1 - 1, recursions + 1, r);
        growBranch(l1.clone().add(diff), diff.clone().multiply(0.7).rotateAroundZ(FastMath.toRadians(runningAngle + getNoise(r))).rotateAroundX(FastMath.toRadians(getNoise(r))),
                d1 - 1, recursions + 1, r);
        growBranch(l1.clone().add(diff), diff.clone().multiply(0.7).rotateAroundZ(FastMath.toRadians(-runningAngle + getNoise(r))).rotateAroundX(FastMath.toRadians(getNoise(r))),
                d1 - 1, recursions + 1, r);
    }

    private int getNoise(Random r) {
        return r.nextInt(90) - 45;
    }
}
