package com.dfsek.terra.bukkit.world;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.util.collections.MaterialSet;
import com.dfsek.terra.profiler.ProfileFrame;
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
        return switch(type) {
            case CRIMSON_FUNGUS -> MaterialSet.get(handle.createBlockData("minecraft:crimson_nylium"));
            case WARPED_FUNGUS -> MaterialSet.get(handle.createBlockData("minecraft:warped_nylium"));
            case BROWN_MUSHROOM, RED_MUSHROOM -> MaterialSet.get(handle.createBlockData("minecraft:mycelium"), handle.createBlockData("minecraft:grass_block"),
                    handle.createBlockData("minecraft:podzol"));
            case CHORUS_PLANT -> MaterialSet.get(handle.createBlockData("minecraft:end_stone"));
            default -> MaterialSet.get(handle.createBlockData("minecraft:grass_block"), handle.createBlockData("minecraft:dirt"),
                    handle.createBlockData("minecraft:podzol"));
        };
    }

    @Override
    @SuppressWarnings("try")
    public boolean plant(Location l, Random r) {
        try(ProfileFrame ignore = main.getProfiler().profile("bukkit_tree:" + delegate.toString().toLowerCase(Locale.ROOT))) {
            return ((BukkitWorld) l.getWorld()).getHandle().generateTree(BukkitAdapter.adapt(l), delegate);
        }
    }

    @Override
    public MaterialSet getSpawnable() {
        return spawnable;
    }
}
