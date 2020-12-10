package com.dfsek.terra.api.gaea.tree.fractal.trees;

import com.dfsek.terra.api.gaea.math.FastNoiseLite;
import com.dfsek.terra.api.gaea.math.ProbabilityCollection;
import com.dfsek.terra.api.gaea.tree.fractal.FractalTree;
import com.dfsek.terra.api.gaea.tree.fractal.TreeGeometry;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Random;


public class Rock extends FractalTree {
    private final TreeGeometry geo;
    private final FastNoiseLite noise;
    private final FastNoiseLite rock;
    private static final ProbabilityCollection<Material> ice = new ProbabilityCollection<Material>().add(Material.PACKED_ICE, 95).add(Material.BLUE_ICE, 5);

    /**
     * Instantiates a TreeGrower at an origin location.
     *
     * @param origin - The origin location.
     * @param random - The random object to use whilst generating the tree.
     */
    public Rock(Location origin, Random random) {
        super(origin, random);
        int seed = origin.hashCode();
        this.noise = new FastNoiseLite(seed);
        this.rock = new FastNoiseLite(++seed);
        geo = new TreeGeometry(this);
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    @Override
    public void grow() {
        Location l1 = super.getOrigin().clone();
        int h = super.getRandom().nextInt(16) + 8;
        for(int i = 0; i < h; i++) {
            geo.generateSponge(l1.clone().add(0, i, 0), ice, (int) ((1-((double) i / h))*2+1), true, 70);
        }
        for(int i = 0; i < h/3; i++) {
            setBlock(l1.clone().add(0, h+i, 0), ice.get(super.getRandom()));
        }
    }
}
