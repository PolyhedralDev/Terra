package com.dfsek.terra.bukkit.population;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.api.util.GlueList;
import com.dfsek.terra.api.world.generation.TerraBlockPopulator;
import com.dfsek.terra.profiler.ProfileFuture;
import com.dfsek.terra.profiler.WorldProfiler;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Cursed management class for the horrors of Bukkit population
 */
public class PopulationManager implements TerraBlockPopulator {
    private final List<TerraBlockPopulator> attachedPopulators = new GlueList<>();
    private final HashSet<ChunkCoordinate> needsPop = new HashSet<>();
    private final TerraPlugin main;
    private WorldProfiler profiler;

    public PopulationManager(TerraPlugin main) {
        this.main = main;
    }

    public void attach(TerraBlockPopulator populator) {
        this.attachedPopulators.add(populator);
    }

    @Override
    @SuppressWarnings("try")
    public void populate(@NotNull World world, @NotNull Chunk chunk) {
        try(ProfileFuture ignored = measure()) {
            needsPop.add(new ChunkCoordinate(chunk));
            int x = chunk.getX();
            int z = chunk.getZ();
            if(main.isEnabled()) {
                for(int xi = -1; xi <= 1; xi++) {
                    for(int zi = -1; zi <= 1; zi++) {
                        if(xi == 0 && zi == 0) continue;
                        if(world.isChunkGenerated(xi + x, zi + z)) checkNeighbors(xi + x, zi + z, world);
                    }
                }
            }
        }
    }

    private ProfileFuture measure() {
        if(profiler != null) return profiler.measure("PopulationManagerTime");
        return null;
    }

    public void attachProfiler(WorldProfiler p) {
        this.profiler = p;
    }

    @SuppressWarnings("unchecked")
    public synchronized void saveBlocks(World w) throws IOException {
        File f = new File(Gaea.getGaeaFolder(w), "chunks.bin");
        f.createNewFile();
        SerializationUtil.toFile((HashSet<ChunkCoordinate>) needsPop.clone(), f);
    }

    @SuppressWarnings("unchecked")
    public synchronized void loadBlocks(World w) throws IOException, ClassNotFoundException {
        File f = new File(Gaea.getGaeaFolder(w), "chunks.bin");
        needsPop.addAll((HashSet<ChunkCoordinate>) SerializationUtil.fromFile(f));
    }


    // Synchronize to prevent chunks from being queued for population multiple times.
    public synchronized void checkNeighbors(int x, int z, World w) {
        ChunkCoordinate c = new ChunkCoordinate(x, z, w.getUID());
        if(w.isChunkGenerated(x + 1, z)
                && w.isChunkGenerated(x - 1, z)
                && w.isChunkGenerated(x, z + 1)
                && w.isChunkGenerated(x, z - 1) && needsPop.contains(c)) {
            Random random = new FastRandom(w.getSeed());
            long xRand = (random.nextLong() / 2L << 1L) + 1L;
            long zRand = (random.nextLong() / 2L << 1L) + 1L;
            random.setSeed((long) x * xRand + (long) z * zRand ^ w.getSeed());
            Chunk currentChunk = w.getChunkAt(x, z);
            for(TerraBlockPopulator r : attachedPopulators) {
                r.populate(w, currentChunk);
            }
            needsPop.remove(c);
        }
    }
}