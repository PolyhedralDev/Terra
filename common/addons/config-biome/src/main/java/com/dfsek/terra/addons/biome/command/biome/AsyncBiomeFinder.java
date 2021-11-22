/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.command.biome;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


/**
 * Runnable that locates a biome asynchronously
 */
public class AsyncBiomeFinder implements Runnable {
    
    protected final BiomeProvider provider;
    protected final TerraBiome target;
    protected final int startRadius;
    protected final int maxRadius;
    protected final int centerX;
    protected final int centerZ;
    protected final World world;
    protected final Platform platform;
    private final Consumer<Vector3> callback;
    protected int searchSize = 1;
    
    public AsyncBiomeFinder(BiomeProvider provider, TerraBiome target, @NotNull Vector3 origin, World world, int startRadius, int maxRadius,
                            Consumer<Vector3> callback, Platform platform) {
        this.provider = provider;
        this.target = target;
        this.platform = platform;
        this.startRadius = startRadius;
        this.maxRadius = maxRadius;
        this.centerX = origin.getBlockX();
        this.centerZ = origin.getBlockZ();
        this.world = world;
        this.callback = callback;
    }
    
    public Vector3 finalizeVector(Vector3 orig) {
        return orig.multiply(platform.getTerraConfig().getBiomeSearchResolution());
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
        callback.accept(finalSpawn);
    }
    
    /**
     * Helper method to get biome at location
     *
     * @param x X coordinate
     * @param z Z coordinate
     *
     * @return TerraBiome at coordinates
     */
    public boolean isValid(int x, int z, TerraBiome target) {
        int res = platform.getTerraConfig().getBiomeSearchResolution();
        return getProvider().getBiome(x * res, z * res, world.getSeed()).equals(target);
    }
    
    public TerraBiome getTarget() {
        return target;
    }
    
    public World getWorld() {
        return world;
    }
    
    public BiomeProvider getProvider() {
        return provider;
    }
    
    public int getSearchSize() {
        return searchSize;
    }
    
    public void setSearchSize(int searchSize) {
        this.searchSize = searchSize;
    }
}
