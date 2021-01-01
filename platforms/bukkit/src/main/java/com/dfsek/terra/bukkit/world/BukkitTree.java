package com.dfsek.terra.bukkit.world;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.block.MaterialData;
import com.dfsek.terra.api.platform.handle.WorldHandle;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.bukkit.util.BukkitConversions;
import com.dfsek.terra.util.MaterialSet;
import org.bukkit.TreeType;

import java.util.Random;
import java.util.Set;

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
                return MaterialSet.get(handle.createMaterialData("minecraft:crimson_nylium"));
            case WARPED_FUNGUS:
                return MaterialSet.get(handle.createMaterialData("minecraft:warped_nylium"));
            case BROWN_MUSHROOM:
            case RED_MUSHROOM:
                return MaterialSet.get(handle.createMaterialData("minecraft:mycelium"), handle.createMaterialData("minecraft:grass_block"),
                        handle.createMaterialData("minecraft:podzol"));
            case CHORUS_PLANT:
                return MaterialSet.get(handle.createMaterialData("minecraft:end_stone"));
            default:
                return MaterialSet.get(handle.createMaterialData("minecraft:grass_block"), handle.createMaterialData("minecraft:dirt"),
                        handle.createMaterialData("minecraft:podzol"));
        }
    }

    @Override
    public TreeType getHandle() {
        return delegate;
    }

    @Override
    public boolean plant(Location l, Random r) {
        return ((BukkitWorld) l.getWorld()).getHandle().generateTree(BukkitConversions.toBukkitLocation(l), delegate);
    }

    @Override
    public Set<MaterialData> getSpawnable() {
        return spawnable;
    }
}
