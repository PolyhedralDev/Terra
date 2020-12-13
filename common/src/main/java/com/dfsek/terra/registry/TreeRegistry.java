package com.dfsek.terra.registry;

import com.dfsek.terra.api.gaea.tree.Tree;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.world.block.MaterialData;
import com.dfsek.terra.api.generic.world.vector.Location;

import java.util.Random;
import java.util.Set;

public class TreeRegistry extends TerraRegistry<Tree> {
    private final TerraPlugin main;

    public TreeRegistry(TerraPlugin main) {
        this.main = main;
        addTree("ACACIA");
        addTree("BIRCH");
        addTree("BROWN_MUSHROOM");
    }

    private void addTree(String id) {
        try {
            add(id, main.getWorldHandle().getTree(id));
        } catch(IllegalArgumentException e) {
            main.getLogger().warning("Unable to load tree " + id + ": " + e.getMessage());
        }
    }

    private static final class FractalTreeHolder implements Tree {

        @Override
        public boolean plant(Location l, Random r) {
            return false;
        }

        @Override
        public Set<MaterialData> getSpawnable() {
            return null;
        }
    }
}
