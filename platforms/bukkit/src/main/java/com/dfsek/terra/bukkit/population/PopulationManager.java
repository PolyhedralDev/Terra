/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.bukkit.population;

import com.dfsek.terra.api.world.chunk.Chunk;

import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.profiler.ProfileFrame;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.stage.Chunkified;
import com.dfsek.terra.bukkit.PlatformImpl;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.bukkit.world.BukkitWorld;
import com.dfsek.terra.util.FastRandom;


/**
 * Cursed management class for the horrors of Bukkit population
 */
public class PopulationManager extends BlockPopulator {
    private final ChunkGenerator generator;
    private final HashSet<ChunkCoordinate> needsPop = new HashSet<>();
    private final Platform platform;
    
    public PopulationManager(ChunkGenerator generator, Platform platform) {
        this.generator = generator;
        this.platform = platform;
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
        try(ProfileFrame ignore = platform.getProfiler().profile("popman")) {
            Chunk chunk = BukkitAdapter.adapt(source);
            needsPop.add(new ChunkCoordinate(chunk));
            int x = chunk.getX();
            int z = chunk.getZ();
            if(((PlatformImpl) platform).getPlugin().isEnabled()) {
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