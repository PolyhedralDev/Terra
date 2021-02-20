package com.dfsek.terra.api.world.tree.fractal.trees;

import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.util.world.MaterialSet;
import com.dfsek.terra.api.world.tree.fractal.FractalTree;
import com.dfsek.terra.api.world.tree.fractal.TreeGeometry;
import net.jafama.FastMath;

import java.util.Random;

public class SpruceTree extends FractalTree {
    private final TreeGeometry geo;

    @Override
    public MaterialSet getSpawnable() {
        return MaterialSet.get(main.getWorldHandle().createMaterialData("minecraft:podzol"),
                main.getWorldHandle().createMaterialData("minecraft:grass_block"));
    }

    /**
     * Instantiates a TreeGrower at an origin location.
     */
    public SpruceTree(TerraPlugin main) {
        super(main);
        geo = new TreeGeometry(this);
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    @Override
    public void grow(Location origin, Random random) {
        growTrunk(origin.clone(), new Vector3(0, 16 + random.nextInt(5), 0), random);
    }

    private void growTrunk(Location l1, Vector3 diff, Random r) {
        if(diff.getY() < 0) diff.rotateAroundAxis(TreeGeometry.getPerpendicular(diff.clone()).normalize(), FastMath.PI);
        int d = (int) diff.length();
        int rad = 7;
        BlockData wood = getMain().getWorldHandle().createBlockData("minecraft:spruce_wood");
        BlockData leaves = getMain().getWorldHandle().createBlockData("minecraft:spruce_leave");
        for(int i = 0; i < d; i++) {
            geo.generateSphere(l1.clone().add(diff.clone().multiply((double) i / d)), wood, (int) ((i > d * 0.65) ? 0.5 : 1.5), true, r);
            if(i > 3)
                geo.generateCylinder(l1.clone().add(diff.clone().multiply((double) i / d)), leaves, (int) (((6 - (i % 4))) * (1.25 - ((double) i / d))), 1, false, r);
        }
        setBlock(l1.clone().add(diff), leaves);
        setBlock(l1.clone().add(diff).add(0, 1, 0), leaves);
    }
}
