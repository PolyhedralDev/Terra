package com.dfsek.terra.bukkit.generator;

import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.platform.generator.GeneratorWrapper;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.world.generation.TerraChunkGenerator;
import com.dfsek.terra.api.world.generation.population.PopulationManager;
import com.dfsek.terra.bukkit.BukkitBiomeGrid;
import com.dfsek.terra.bukkit.BukkitWorld;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.debug.Debug;
import com.dfsek.terra.generation.MasterChunkGenerator;
import com.dfsek.terra.population.CavePopulator;
import com.dfsek.terra.population.FloraPopulator;
import com.dfsek.terra.population.OrePopulator;
import com.dfsek.terra.population.StructurePopulator;
import com.dfsek.terra.population.TreePopulator;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BukkitChunkGeneratorWrapper extends ChunkGenerator implements GeneratorWrapper {

    private static final Map<com.dfsek.terra.api.platform.world.World, PopulationManager> popMap = new HashMap<>();

    private final PopulationManager popMan;

    private final TerraChunkGenerator delegate;

    private final TerraPlugin main;

    private boolean needsLoad = true;

    public BukkitChunkGeneratorWrapper(TerraChunkGenerator delegate) {
        this.delegate = delegate;
        this.main = delegate.getMain();
        popMan = new PopulationManager(main);
        popMan.attach(new OrePopulator(main));
        popMan.attach(new TreePopulator(main));
        popMan.attach(new FloraPopulator(main));
    }


    public static synchronized void saveAll() {
        for(Map.Entry<com.dfsek.terra.api.platform.world.World, PopulationManager> e : popMap.entrySet()) {
            try {
                e.getValue().saveBlocks(e.getKey());
                Debug.info("Saved data for world " + e.getKey().getName());
            } catch(IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static synchronized void fixChunk(Chunk c) {
        if(!(c.getWorld().getGenerator() instanceof MasterChunkGenerator)) throw new IllegalArgumentException();
        popMap.get(c.getWorld()).checkNeighbors(c.getX(), c.getZ(), c.getWorld());
    }

    private void load(com.dfsek.terra.api.platform.world.World w) {
        try {
            popMan.loadBlocks(w);
        } catch(FileNotFoundException e) {
            LangUtil.log("warning.no-population", Level.WARNING);
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        popMap.put(w, popMan);
        needsLoad = false;
    }

    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull BiomeGrid biome) {
        BukkitWorld bukkitWorld = new BukkitWorld(world);
        if(needsLoad) load(bukkitWorld); // Load population data for world.
        delegate.generateBiomes(bukkitWorld, random, x, z, new BukkitBiomeGrid(biome));
        return (ChunkData) delegate.generateChunkData(bukkitWorld, random, x, z, new BukkitChunkGenerator.BukkitChunkData(createChunkData(world))).getHandle();
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return Stream.of(new CavePopulator(main), new StructurePopulator(main), popMan).map(BukkitPopulatorWrapper::new).collect(Collectors.toList());
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
        return super.shouldGenerateStructures();
    }

    @Override
    public TerraChunkGenerator getHandle() {
        return delegate;
    }
}
