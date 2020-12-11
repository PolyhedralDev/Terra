package com.dfsek.terra.structure.spawn;

import com.dfsek.terra.api.bukkit.TerraBukkitPlugin;
import com.dfsek.terra.api.generic.world.World;

public abstract class Requirement {
    protected final World world;
    protected final TerraBukkitPlugin main;

    public Requirement(World world, TerraBukkitPlugin main) {
        this.world = world;
        this.main = main;
    }

    public abstract boolean matches(int x, int y, int z);


    public World getWorld() {
        return world;
    }
}
