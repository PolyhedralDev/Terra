package com.dfsek.terra.async;

import com.dfsek.terra.api.generic.world.vector.Vector3;
import com.dfsek.terra.api.implementations.bukkit.TerraBukkitPlugin;
import com.dfsek.terra.biome.grid.master.TerraBiomeGrid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class AsyncFeatureFinder<T> implements Runnable {
    protected final TerraBiomeGrid grid;
    protected final T target;
    protected final int startRadius;
    protected final int maxRadius;
    protected final int centerX;
    protected final int centerZ;
    protected final World world;
    private final Consumer<Vector3> callback;
    protected int searchSize = 1;
    protected final TerraBukkitPlugin main;

    public AsyncFeatureFinder(TerraBiomeGrid grid, T target, @NotNull Location origin, int startRadius, int maxRadius, Consumer<Vector3> callback, TerraBukkitPlugin main) {
        this.grid = grid;
        this.target = target;
        this.main = main;
        this.startRadius = startRadius;
        this.maxRadius = maxRadius;
        this.centerX = origin.getBlockX();
        this.centerZ = origin.getBlockZ();
        this.world = origin.getWorld();
        this.callback = callback;
    }

    @Override
    public void run() {
        int x = centerX;
        int z = centerZ;

        x /= searchSize;
        z /= searchSize;

        int run = 1;
        boolean toggle = true;
        boolean found = false;

        main:
        for(int i = startRadius; i < maxRadius; i++) {
            for(int j = 0; j < run; j++) {
                if(isValid(x, z, target)) {
                    found = true;
                    break main;
                }
                if(toggle) x += 1;
                else x -= 1;
            }
            for(int j = 0; j < run; j++) {
                if(isValid(x, z, target)) {
                    found = true;
                    break main;
                }
                if(toggle) z += 1;
                else z -= 1;
            }
            run++;
            toggle = !toggle;
        }
        Vector3 finalSpawn = found ? finalizeVector(new Vector3(x, 0, z)) : null;
        Bukkit.getScheduler().runTask(main, () -> callback.accept(finalSpawn));
    }


    public abstract Vector3 finalizeVector(Vector3 orig);

    public abstract boolean isValid(int x, int z, T target);

    public T getTarget() {
        return target;
    }

    public World getWorld() {
        return world;
    }

    public TerraBiomeGrid getGrid() {
        return grid;
    }

    public int getSearchSize() {
        return searchSize;
    }

    public void setSearchSize(int searchSize) {
        this.searchSize = searchSize;
    }
}
