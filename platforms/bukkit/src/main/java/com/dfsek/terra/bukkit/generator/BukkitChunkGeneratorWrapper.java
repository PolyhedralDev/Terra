package com.dfsek.terra.bukkit.generator;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.generator.GeneratorWrapper;
import com.dfsek.terra.api.world.generation.TerraBlockPopulator;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import com.dfsek.terra.bukkit.population.PopulationManager;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.bukkit.world.BukkitBiomeGrid;
import com.dfsek.terra.profiler.DataType;
import com.dfsek.terra.profiler.Measurement;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.population.CavePopulator;
import com.dfsek.terra.world.population.FloraPopulator;
import com.dfsek.terra.world.population.OrePopulator;
import com.dfsek.terra.world.population.StructurePopulator;
import com.dfsek.terra.world.population.TreePopulator;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class BukkitChunkGeneratorWrapper extends ChunkGenerator implements GeneratorWrapper {

    private static final Map<com.dfsek.terra.api.platform.world.World, PopulationManager> popMap = new HashMap<>();

    private final PopulationManager popMan;

    private final TerraChunkGenerator delegate;

    private final TerraPlugin main;

    private final List<TerraBlockPopulator> populators = new LinkedList<>();

    private boolean needsLoad = true;

    public BukkitChunkGeneratorWrapper(TerraChunkGenerator delegate) {
        this.delegate = delegate;
        this.main = delegate.getMain();
        popMan = new PopulationManager(main);
        popMan.attach(new OrePopulator(main));
        popMan.attach(new TreePopulator(main));
        popMan.attach(new FloraPopulator(main));
        populators.add(new CavePopulator(main));
        populators.add(new StructurePopulator(main));
        populators.add(popMan);
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
        if(!TerraWorld.isTerraWorld(c.getWorld())) throw new IllegalArgumentException();
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
        main.getWorld(w).getProfiler().addMeasurement(new Measurement(15000000, DataType.PERIOD_MILLISECONDS), "PopulationManagerTime");
        popMan.attachProfiler(main.getWorld(w).getProfiler());
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
        return populators.stream().map(BukkitPopulatorWrapper::new).collect(Collectors.toList());
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
