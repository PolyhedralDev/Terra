package com.dfsek.terra.api.gaea.tree.fractal.trees;

import com.dfsek.terra.api.gaea.math.ProbabilityCollection;
import com.dfsek.terra.api.gaea.tree.fractal.FractalTree;
import com.dfsek.terra.api.gaea.tree.fractal.TreeGeometry;
import com.dfsek.terra.api.generic.world.vector.Location;
import com.dfsek.terra.api.generic.world.vector.Vector3;
import org.bukkit.Material;

import java.util.Random;


public class IceSpike extends FractalTree {
    private final TreeGeometry geo;
    private static final ProbabilityCollection<Material> ice = new ProbabilityCollection<Material>().add(Material.PACKED_ICE, 95).add(Material.BLUE_ICE, 5);

    /**
     * Instantiates a TreeGrower at an origin location.
     *
     * @param origin - The origin location.
     * @param random - The random object to use whilst generating the tree.
     */
    public IceSpike(Location origin, Random random) {
        super(origin, random);
        geo = new TreeGeometry(this);
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
