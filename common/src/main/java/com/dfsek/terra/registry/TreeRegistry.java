package com.dfsek.terra.registry;

import com.dfsek.terra.api.gaea.tree.Tree;
import com.dfsek.terra.api.gaea.tree.fractal.FractalTree;
import com.dfsek.terra.api.gaea.tree.fractal.trees.Cactus;
import com.dfsek.terra.api.gaea.tree.fractal.trees.OakTree;
import com.dfsek.terra.api.gaea.tree.fractal.trees.ShatteredPillar;
import com.dfsek.terra.api.gaea.tree.fractal.trees.ShatteredTree;
import com.dfsek.terra.api.gaea.tree.fractal.trees.SmallShatteredPillar;
import com.dfsek.terra.api.gaea.tree.fractal.trees.SmallShatteredTree;
import com.dfsek.terra.api.gaea.tree.fractal.trees.SpruceTree;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.world.WorldHandle;
import com.dfsek.terra.api.generic.world.block.MaterialData;
import com.dfsek.terra.api.generic.world.vector.Location;
import com.dfsek.terra.util.MaterialSet;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import java.util.Set;

public class TreeRegistry extends TerraRegistry<Tree> {
    private final TerraPlugin main;

    public TreeRegistry(TerraPlugin main) {
        this.main = main;
        addTree("ACACIA");
        addTree("BIRCH");
        addTree("BROWN_MUSHROOM");
        tryAdd("CACTUS", Cactus.class);
        addTree("CHORUS_PLANT");
        addTree("DARK_OAK");
        tryAdd("GIANT_OAK", OakTree.class);
        tryAdd("GIANT_SPRUCE", SpruceTree.class);
        addTree("JUNGLE");
        addTree("JUNGLE_COCOA");
        addTree("JUNGLE_BUSH");
        addTree("LARGE_OAK");
        tryAdd("LARGE_SHATTERED_PILLAR", ShatteredPillar.class);
        addTree("LARGE_SPRUCE");
        addTree("MEGA_SPRUCE");
        addTree("OAK");
        addTree("RED_MUSHROOM");
        tryAdd("SHATTERED_LARGE", ShatteredTree.class);
        tryAdd("SHATTERED_SMALL", SmallShatteredTree.class);
        addTree("SMALL_JUNGLE");
        addTree("SPRUCE");
        addTree("SWAMP_OAK");
        tryAdd("SMALL_SHATTERED_PILLAR", SmallShatteredPillar.class);
        addTree("TALL_BIRCH");
    }

    private void addTree(String id) {
        try {
            add(id, main.getWorldHandle().getTree(id));
        } catch(IllegalArgumentException e) {
            main.getLogger().warning("Unable to load tree " + id + ": " + e.getMessage());
        }
    }

    private void tryAdd(String id, Class<? extends FractalTree> value) {
        try {
            add(id, new FractalTreeHolder(value));
        } catch(Exception e) {
            main.getLogger().warning("Unable to load tree " + id + ": " + e.getMessage());
        }
    }

    private final class FractalTreeHolder implements Tree { // TODO: this is jank and should be replaced later.
        private final Constructor<? extends FractalTree> constructor;
        private final MaterialSet set;

        private FractalTreeHolder(Class<? extends FractalTree> clazz) throws NoSuchMethodException {
            constructor = clazz.getConstructor(Location.class, Random.class, TerraPlugin.class);
            WorldHandle h = main.getWorldHandle();
            set = MaterialSet.get(h.createMaterialData("minecraft:grass_block"), h.createMaterialData("minecraft:snow_block")); // TODO: actually implement
        }

        @Override
        public boolean plant(Location l, Random r) {
            try {
                FractalTree tree = constructor.newInstance(l, r, main);
                if(!getSpawnable().contains(l.subtract(0, 1, 0).getBlock().getType())) return false;
                tree.grow();
                tree.plant();
                return true;
            } catch(InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        public Set<MaterialData> getSpawnable() {
            return set;
        }
    }
}
