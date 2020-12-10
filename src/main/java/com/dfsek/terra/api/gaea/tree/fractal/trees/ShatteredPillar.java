package com.dfsek.terra.api.gaea.tree.fractal.trees;

import com.dfsek.terra.api.gaea.tree.fractal.FractalTree;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EnderCrystal;

import java.util.Random;

public class ShatteredPillar extends FractalTree {
    /**
     * Instantiates a TreeGrower at an origin location.
     *
     * @param origin - The origin location.
     * @param random - The random object to use whilst generating the tree.
     */
    public ShatteredPillar(Location origin, Random random) {
        super(origin, random);
    }

    /**
     * Grows the tree in memory. Intended to be invoked from an async thread.
     */
    @Override
    public void grow() {
        int h = super.getRandom().nextInt(5) + 8;
        int max = h;
        for(int i = - h; i < h; i++) setBlock(super.getOrigin().clone().add(0, i, 0), Material.OBSIDIAN);
        h = h + (getRandom().nextBoolean() ? getRandom().nextInt(3) + 1 : - (getRandom().nextInt(3) + 1));
        int[] crystalLoc = new int[] {0, 0};
        if(h > max) {
            max = h;
            crystalLoc = new int[] {1, 0};
        }
        for(int i = - h; i < h; i++) setBlock(super.getOrigin().clone().add(1, i, 0), Material.OBSIDIAN);
        h = h + (getRandom().nextBoolean() ? getRandom().nextInt(3) + 1 : - (getRandom().nextInt(3) + 1));
        if(h > max) {
            max = h;
            crystalLoc = new int[] {0, 1};
        }
        for(int i = - h; i < h; i++) setBlock(super.getOrigin().clone().add(0, i, 1), Material.OBSIDIAN);
        h = h + (getRandom().nextBoolean() ? getRandom().nextInt(3) + 1 : - (getRandom().nextInt(3) + 1));
        if(h > max) {
            max = h;
            crystalLoc = new int[] {1, 1};
        }
        for(int i = - h; i < h; i++) setBlock(super.getOrigin().clone().add(1, i, 1), Material.OBSIDIAN);
        if(getRandom().nextInt(100) < 25)
            spawnEntity(getOrigin().add(crystalLoc[0] + 0.5, max, crystalLoc[1] + 0.5), EnderCrystal.class,
                    enderCrystal -> ((EnderCrystal) enderCrystal).setShowingBottom(false));
    }
}
