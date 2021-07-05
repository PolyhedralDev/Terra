package com.dfsek.terra.bukkit.world;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.util.collections.MaterialSetImpl;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.api.world.World;
import org.bukkit.TreeType;

import java.util.Locale;
import java.util.Random;

public class BukkitTree implements Tree {
    private final TreeType delegate;
    private final MaterialSetImpl spawnable;
    private final TerraPlugin main;

    public BukkitTree(TreeType delegate, TerraPlugin main) {
        this.delegate = delegate;
        this.main = main;
        this.spawnable = getSpawnable(delegate);
    }

    private MaterialSetImpl getSpawnable(TreeType type) {
        WorldHandle handle = main.getWorldHandle();
        switch(type) {
            case CRIMSON_FUNGUS:
                return MaterialSetImpl.get(handle.createBlockData("minecraft:crimson_nylium"));
            case WARPED_FUNGUS:
                return MaterialSetImpl.get(handle.createBlockData("minecraft:warped_nylium"));
            case BROWN_MUSHROOM:
            case RED_MUSHROOM:
                return MaterialSetImpl.get(handle.createBlockData("minecraft:mycelium"), handle.createBlockData("minecraft:grass_block"),
                        handle.createBlockData("minecraft:podzol"));
            case CHORUS_PLANT:
                return MaterialSetImpl.get(handle.createBlockData("minecraft:end_stone"));
            default:
                return MaterialSetImpl.get(handle.createBlockData("minecraft:grass_block"), handle.createBlockData("minecraft:dirt"),
                        handle.createBlockData("minecraft:podzol"));
        }
    }

    @Override
    @SuppressWarnings("try")
    public boolean plant(Vector3 l, World world, Random r) {
        try(ProfileFrame ignore = main.getProfiler().profile("bukkit_tree:" + delegate.toString().toLowerCase(Locale.ROOT))) {
            return BukkitAdapter.adapt(world).generateTree(BukkitAdapter.adapt(l).toLocation(BukkitAdapter.adapt(world)), delegate);
        }
    }

    @Override
    public MaterialSetImpl getSpawnable() {
        return spawnable;
    }
}
