package com.dfsek.terra.api.gaea.tree.fractal.trees;

import com.dfsek.terra.api.gaea.tree.fractal.FractalTree;
import com.dfsek.terra.api.gaea.tree.fractal.TreeGeometry;
import com.dfsek.terra.api.generic.world.vector.Location;
import com.dfsek.terra.api.generic.world.vector.Vector3;
import net.jafama.FastMath;
import org.bukkit.Material;

import java.util.Random;

public class SpruceTree extends FractalTree {
    private final TreeGeometry geo;

    /**
     * Instantiates a TreeGrower at an origin location.
     *
     * @param origin - The origin location.
     * @param random - The random object to use whilst generating the tree.
     */
    public SpruceTree(Location origin, Random random) {
        super(origin, random);
        geo = new TreeGeometry(this);
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    @Override
    public void grow() {
        growTrunk(super.getOrigin().clone(), new Vector3(0, 16 + super.getRandom().nextInt(5), 0));
    }

    private void growTrunk(Location l1, Vector3 diff) {
        if(diff.getY() < 0) diff.rotateAroundAxis(TreeGeometry.getPerpendicular(diff.clone()).normalize(), FastMath.PI);
        int d = (int) diff.length();
        int rad = 7;
        for(int i = 0; i < d; i++) {
            geo.generateSphere(l1.clone().add(diff.clone().multiply((double) i / d)), Material.SPRUCE_WOOD, (int) ((i > d * 0.65) ? 0.5 : 1.5), true);
            if(i > 3)
                geo.generateCylinder(l1.clone().add(diff.clone().multiply((double) i / d)), Material.SPRUCE_LEAVES, (int) (((6 - (i % 4))) * (1.25 - ((double) i / d))), 1, false);
        }
        setBlock(l1.clone().add(diff), Material.SPRUCE_LEAVES);
        setBlock(l1.clone().add(diff).add(0, 1, 0), Material.SPRUCE_LEAVES);
    }
}
