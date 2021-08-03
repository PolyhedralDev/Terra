package com.dfsek.terra.bukkit.population;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.world.Chunk;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.generator.ChunkGenerator;
import com.dfsek.terra.api.world.generator.Chunkified;
import com.dfsek.terra.bukkit.TerraPluginImpl;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.bukkit.world.BukkitWorld;
import com.dfsek.terra.util.FastRandom;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

/**
 * Cursed management class for the horrors of Bukkit population
 */
public class PopulationManager extends BlockPopulator {
    private final ChunkGenerator generator;
    private final HashSet<ChunkCoordinate> needsPop = new HashSet<>();
    private final TerraPlugin main;

    public PopulationManager(ChunkGenerator generator, TerraPlugin main) {
        this.generator = generator;
        this.main = main;
    }

    public static File getDataFolder(World w) {
        File f = new File(((BukkitWorld) w).getWorldFolder(), "gaea");
        f.mkdirs();
        return f;
    }

    @SuppressWarnings("unchecked")
    public synchronized void saveBlocks(World w) throws IOException {
        File f = new File(getDataFolder(w), "chunks.bin");
        f.createNewFile();
        SerializationUtil.toFile((HashSet<ChunkCoordinate>) needsPop.clone(), f);
    }

    @SuppressWarnings("unchecked")
    public synchronized void loadBlocks(World w) throws IOException, ClassNotFoundException {
        File f = new File(getDataFolder(w), "chunks.bin");
        needsPop.addAll((HashSet<ChunkCoordinate>) SerializationUtil.fromFile(f));
    }

    // Synchronize to prevent chunks from being queued for population multiple times.
    public synchronized void checkNeighbors(int x, int z, World world) {
        BukkitWorld w = (BukkitWorld) world;
        ChunkCoordinate c = new ChunkCoordinate(x, z, (w).getUID());
        if(w.isChunkGenerated(x + 1, z)
                && w.isChunkGenerated(x - 1, z)
                && w.isChunkGenerated(x, z + 1)
                && w.isChunkGenerated(x, z - 1) && needsPop.contains(c)) {
            Random random = new FastRandom(w.getSeed());
            long xRand = (random.nextLong() / 2L << 1L) + 1L;
            long zRand = (random.nextLong() / 2L << 1L) + 1L;
            random.setSeed((long) x * xRand + (long) z * zRand ^ w.getSeed());
            Chunk currentChunk = w.getChunkAt(x, z);
            generator.getGenerationStages().forEach(populator -> {
                if(!(populator instanceof Chunkified)) {
                    populator.populate(w, currentChunk);
                }
            });
            needsPop.remove(c);
        }
    }

    @Override
    @SuppressWarnings("try")
    public void populate(org.bukkit.@NotNull World world, @NotNull Random random, org.bukkit.@NotNull Chunk source) {
        try(ProfileFrame ignore = main.getProfiler().profile("popman")) {
            Chunk chunk = BukkitAdapter.adapt(source);
            needsPop.add(new ChunkCoordinate(chunk));
            int x = chunk.getX();
            int z = chunk.getZ();
            if(((TerraPluginImpl) main).getPlugin().isEnabled()) {
                for(int xi = -1; xi <= 1; xi++) {
                    for(int zi = -1; zi <= 1; zi++) {
                        if(xi == 0 && zi == 0) continue;
                        if(world.isChunkGenerated(xi + x, zi + z)) checkNeighbors(xi + x, zi + z, BukkitAdapter.adapt(world));
                    }
                }
            }
        }
    }
}