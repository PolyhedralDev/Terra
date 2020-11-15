package com.dfsek.terra.async;

import com.dfsek.terra.Terra;
import com.dfsek.terra.biome.grid.TerraBiomeGrid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public abstract class AsyncFeatureFinder<T> implements Runnable {
    private final TerraBiomeGrid grid;
    private final T target;
    private final int startRadius;
    private final int maxRadius;
    private final int centerX;
    private final int centerZ;
    private final World world;
    private final Consumer<Vector> callback;
    private int searchSize = 1;

    public AsyncFeatureFinder(TerraBiomeGrid grid, T target, @NotNull Location origin, int startRadius, int maxRadius, Consumer<Vector> callback) {
        this.grid = grid;
        this.target = target;
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
        Vector finalSpawn = found ? finalizeVector(new Vector(x, 0, z)) : null;
        Bukkit.getScheduler().runTask(Terra.getInstance(), () -> callback.accept(finalSpawn));
    }


    public abstract Vector finalizeVector(Vector orig);

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
