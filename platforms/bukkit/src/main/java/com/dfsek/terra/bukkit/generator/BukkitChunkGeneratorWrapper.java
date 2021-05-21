package com.dfsek.terra.bukkit.generator;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.generator.GeneratorWrapper;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import com.dfsek.terra.bukkit.population.PopulationManager;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.bukkit.world.BukkitBiomeGrid;
import com.dfsek.terra.world.TerraWorld;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BukkitChunkGeneratorWrapper extends ChunkGenerator implements GeneratorWrapper {

    private static final Map<com.dfsek.terra.api.platform.world.World, PopulationManager> popMap = new HashMap<>();

    private final PopulationManager popMan;

    private final TerraChunkGenerator delegate;

    private final TerraPlugin main;

    private boolean needsLoad = true;

    public BukkitChunkGeneratorWrapper(TerraChunkGenerator delegate) {
        this.delegate = delegate;
        this.main = delegate.getMain();
        this.popMan = new PopulationManager(delegate, main);

    }


    public static synchronized void saveAll() {
        for(Map.Entry<com.dfsek.terra.api.platform.world.World, PopulationManager> e : popMap.entrySet()) {
            try {
                e.getValue().saveBlocks(e.getKey());
            } catch(IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static synchronized void fixChunk(Chunk c) {
        if(!c.getWorld().isTerraWorld()) throw new IllegalArgumentException();
        popMap.get(c.getWorld()).checkNeighbors(c.getX(), c.getZ(), c.getWorld());
    }

    private void load(com.dfsek.terra.api.platform.world.World w) {
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
        com.dfsek.terra.api.platform.world.World bukkitWorld = BukkitAdapter.adapt(world);
        if(needsLoad) load(bukkitWorld); // Load population data for world.
        delegate.generateBiomes(bukkitWorld, random, x, z, new BukkitBiomeGrid(biome));
        return (ChunkData) delegate.generateChunkData(bukkitWorld, random, x, z, new BukkitChunkGenerator.BukkitChunkData(createChunkData(world))).getHandle();
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return Arrays.asList(popMan, new BukkitPopulatorWrapper(delegate));
    }

    @Override
    public boolean isParallelCapable() {
        return delegate.isParallelCapable();
    }

    @Override
    public boolean shouldGenerateCaves() {
        return delegate.shouldGenerateCaves();
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return delegate.shouldGenerateDecorations();
    }

    @Override
    public boolean shouldGenerateMobs() {
        return delegate.shouldGenerateMobs();
    }

    @Override
    public boolean shouldGenerateStructures() {
        return delegate.shouldGenerateStructures();
    }

    @Override
    public TerraChunkGenerator getHandle() {
        return delegate;
    }
}
