package com.dfsek.terra.bukkit.generator;

import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.WorldConfig;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.generator.ChunkGenerator;
import com.dfsek.terra.api.world.generator.GeneratorWrapper;
import com.dfsek.terra.bukkit.population.PopulationManager;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.bukkit.world.BukkitBiomeGrid;


public class BukkitChunkGeneratorWrapper extends org.bukkit.generator.ChunkGenerator implements GeneratorWrapper {
    
    private static final Map<com.dfsek.terra.api.world.World, PopulationManager> popMap = new HashMap<>();
    
    private final PopulationManager popMan;
    
    private final ChunkGenerator delegate;
    
    private final Platform platform;
    
    private boolean needsLoad = true;
    
    private WorldConfig worldConfig;
    
    public BukkitChunkGeneratorWrapper(ChunkGenerator delegate) {
        this.delegate = delegate;
        this.platform = delegate.getPlatform();
        this.popMan = new PopulationManager(delegate, platform);
    }
    
    
    public static synchronized void saveAll() {
        for(Map.Entry<com.dfsek.terra.api.world.World, PopulationManager> e : popMap.entrySet()) {
            try {
                e.getValue().saveBlocks(e.getKey());
            } catch(IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    
    public static synchronized void fixChunk(Chunk c) {
        popMap.get(c.getWorld()).checkNeighbors(c.getX(), c.getZ(), c.getWorld());
    }
    
    private void load(com.dfsek.terra.api.world.World w) {
        try {
            popMan.loadBlocks(w);
        } catch(FileNotFoundException ignore) {
        
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        popMap.put(w, popMan);
        needsLoad = false;
    }
    
    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome) {
        if(this.worldConfig == null) {
            this.worldConfig = delegate.getConfigPack().toWorldConfig(BukkitAdapter.adapt(world));
        }
        com.dfsek.terra.api.world.World bukkitWorld = BukkitAdapter.adapt(world);
        if(needsLoad) load(bukkitWorld); // Load population data for world.
        delegate.generateBiomes(bukkitWorld, random, x, z, new BukkitBiomeGrid(biome));
        return (ChunkData) delegate.generateChunkData(bukkitWorld, random, x, z, new BukkitChunkData(createChunkData(world))).getHandle();
    }
    
    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return Arrays.asList(popMan, new BukkitPopulatorWrapper(delegate));
    }
    
    @Override
    public boolean isParallelCapable() {
        return true;
    }
    
    @Override
    public boolean shouldGenerateCaves() {
        return delegate.getConfigPack().vanillaCaves();
    }
    
    @Override
    public boolean shouldGenerateDecorations() {
        return delegate.getConfigPack().vanillaFlora();
    }
    
    @Override
    public boolean shouldGenerateMobs() {
        return delegate.getConfigPack().vanillaMobs();
    }
    
    @Override
    public boolean shouldGenerateStructures() {
        return delegate.getConfigPack().vanillaStructures();
    }
    
    public WorldConfig getWorldConfig() {
        return worldConfig;
    }
    
    @Override
    public ChunkGenerator getHandle() {
        return delegate;
    }
}
