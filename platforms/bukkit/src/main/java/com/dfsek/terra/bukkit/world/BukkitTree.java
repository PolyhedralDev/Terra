package com.dfsek.terra.bukkit.world;

import org.bukkit.TreeType;

import java.util.Locale;
import java.util.Random;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.util.collection.MaterialSet;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.api.world.World;


public class BukkitTree implements Tree {
    private final TreeType delegate;
    private final MaterialSet spawnable;
    private final Platform main;
    
    public BukkitTree(TreeType delegate, Platform main) {
        this.delegate = delegate;
        this.main = main;
        this.spawnable = getSpawnable(delegate);
    }
    
    private MaterialSet getSpawnable(TreeType type) {
        WorldHandle handle = main.getWorldHandle();
        return switch(type) {
            case CRIMSON_FUNGUS -> MaterialSet.get(handle.createBlockData("minecraft:crimson_nylium"));
            case WARPED_FUNGUS -> MaterialSet.get(handle.createBlockData("minecraft:warped_nylium"));
            case BROWN_MUSHROOM, RED_MUSHROOM -> MaterialSet.get(handle.createBlockData("minecraft:mycelium"),
                                                                 handle.createBlockData("minecraft:grass_block"),
                                                                 handle.createBlockData("minecraft:podzol"));
            case CHORUS_PLANT -> MaterialSet.get(handle.createBlockData("minecraft:end_stone"));
            default -> MaterialSet.get(handle.createBlockData("minecraft:grass_block"), handle.createBlockData("minecraft:dirt"),
                                       handle.createBlockData("minecraft:podzol"));
        };
    }
    
    @Override
    @SuppressWarnings("try")
    public boolean plant(Vector3 l, World world, Random r) {
        try(ProfileFrame ignore = main.getProfiler().profile("bukkit_tree:" + delegate.toString().toLowerCase(Locale.ROOT))) {
            return BukkitAdapter.adapt(world).generateTree(BukkitAdapter.adapt(l).toLocation(BukkitAdapter.adapt(world)), delegate);
        }
    }
    
    @Override
    public MaterialSet getSpawnable() {
        return spawnable;
    }
}
