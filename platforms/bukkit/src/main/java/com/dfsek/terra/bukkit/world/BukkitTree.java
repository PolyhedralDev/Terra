package com.dfsek.terra.bukkit.world;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.vector.LocationImpl;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.api.profiler.ProfileFrame;
import org.bukkit.TreeType;

import java.util.Locale;
import java.util.Random;

public class BukkitTree implements Tree {
    private final TreeType delegate;
    private final MaterialSet spawnable;
    private final TerraPlugin main;

    public BukkitTree(TreeType delegate, TerraPlugin main) {
        this.delegate = delegate;
        this.main = main;
        this.spawnable = getSpawnable(delegate);
    }

    private MaterialSet getSpawnable(TreeType type) {
        WorldHandle handle = main.getWorldHandle();
        switch(type) {
            case CRIMSON_FUNGUS:
                return MaterialSet.get(handle.createBlockData("minecraft:crimson_nylium"));
            case WARPED_FUNGUS:
                return MaterialSet.get(handle.createBlockData("minecraft:warped_nylium"));
            case BROWN_MUSHROOM:
            case RED_MUSHROOM:
                return MaterialSet.get(handle.createBlockData("minecraft:mycelium"), handle.createBlockData("minecraft:grass_block"),
                        handle.createBlockData("minecraft:podzol"));
            case CHORUS_PLANT:
                return MaterialSet.get(handle.createBlockData("minecraft:end_stone"));
            default:
                return MaterialSet.get(handle.createBlockData("minecraft:grass_block"), handle.createBlockData("minecraft:dirt"),
                        handle.createBlockData("minecraft:podzol"));
        }
    }

    @Override
    @SuppressWarnings("try")
    public boolean plant(LocationImpl l, Random r) {
        try(ProfileFrame ignore = main.getProfiler().profile("bukkit_tree:" + delegate.toString().toLowerCase(Locale.ROOT))) {
            return ((BukkitWorld) l.getWorld()).getHandle().generateTree(BukkitAdapter.adapt(l), delegate);
        }
    }

    @Override
    public MaterialSet getSpawnable() {
        return spawnable;
    }
}
