package com.dfsek.terra.addons.structure.command;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.structure.configured.ConfiguredStructure;
import com.dfsek.terra.api.vector.Vector3;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class AsyncStructureFinder implements Runnable {
    protected final BiomeProvider provider;
    protected final ConfiguredStructure target;
    protected final int startRadius;
    protected final int maxRadius;
    protected final int centerX;
    protected final int centerZ;
    protected final World world;
    protected final TerraPlugin main;
    private final Consumer<Vector3> callback;
    protected int searchSize = 1;
    
    public AsyncStructureFinder(BiomeProvider provider, ConfiguredStructure target, @NotNull Vector3 origin, World world, int startRadius,
                                int maxRadius, Consumer<Vector3> callback, TerraPlugin main) {
        //setSearchSize(target.getSpawn().getWidth() + 2 * target.getSpawn().getSeparation());
        this.provider = provider;
        this.target = target;
        this.main = main;
        this.startRadius = startRadius;
        this.maxRadius = maxRadius;
        this.centerX = origin.getBlockX();
        this.centerZ = origin.getBlockZ();
        this.world = world;
        this.callback = callback;
    }
    
    public Vector3 finalizeVector(Vector3 orig) {
        return orig;//target.getSpawn().getChunkSpawn(orig.getBlockX(), orig.getBlockZ(), world.getSeed());
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
    
    public boolean isValid(int x, int z, ConfiguredStructure target) {
        //Vector3 spawn = target.getSpawn().getChunkSpawn(x, z, world.getSeed());
        //if(!((UserDefinedBiome) provider.getBiome(spawn)).getConfig().getStructures().contains(target)) return false;
        //Random random = new Random(PopulationUtil.getCarverChunkSeed(FastMath.floorDiv(spawn.getBlockX(), 16), FastMath.floorDiv(spawn
        // .getBlockZ(), 16), world.getSeed()));
        //return target.getStructure().get(random).test(spawn.setY(target.getSpawnStart().get(random)), world, random, Rotation
        // .fromDegrees(90 * random.nextInt(4)));
        return false;
    }
    
    public ConfiguredStructure getTarget() {
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
