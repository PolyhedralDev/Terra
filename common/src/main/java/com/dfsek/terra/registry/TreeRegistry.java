package com.dfsek.terra.registry;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.block.BlockFace;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.world.tree.Tree;
import com.dfsek.terra.api.world.tree.fractal.FractalTree;
import com.dfsek.terra.api.world.tree.fractal.trees.Cactus;
import com.dfsek.terra.api.world.tree.fractal.trees.IceSpike;
import com.dfsek.terra.api.world.tree.fractal.trees.OakTree;
import com.dfsek.terra.api.world.tree.fractal.trees.ShatteredPillar;
import com.dfsek.terra.api.world.tree.fractal.trees.ShatteredTree;
import com.dfsek.terra.api.world.tree.fractal.trees.SmallShatteredPillar;
import com.dfsek.terra.api.world.tree.fractal.trees.SmallShatteredTree;
import com.dfsek.terra.api.world.tree.fractal.trees.SpruceTree;

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
        tryAdd("ICE_SPIKE", IceSpike.class);
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

    @Override
    public boolean add(String name, Tree value) {
        return super.add(name, value);
    }

    private final class FractalTreeHolder implements Tree {
        private final FractalTree tree;

        private FractalTreeHolder(Class<? extends FractalTree> clazz) throws NoSuchMethodException {
            Constructor<? extends FractalTree> constructor = clazz.getConstructor(TerraPlugin.class);
            try {
                tree = constructor.newInstance(main);
            } catch(InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Unable to load tree: " + clazz);
            }
        }

        @Override
        public boolean plant(Location l, Random r) {
            if(!getSpawnable().contains(l.getBlock().getType())) return false;
            if(!l.getBlock().getRelative(BlockFace.UP).isEmpty()) return false;
            tree.grow(l.add(0, 1, 0), r);
            return true;
        }

        @Override
        public Set<MaterialData> getSpawnable() {
            return tree.getSpawnable();
        }
    }
}
