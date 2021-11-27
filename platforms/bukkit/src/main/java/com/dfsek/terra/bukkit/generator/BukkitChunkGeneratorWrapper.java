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

package com.dfsek.terra.bukkit.generator;

import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


public class BukkitChunkGeneratorWrapper extends org.bukkit.generator.ChunkGenerator implements GeneratorWrapper {
    private static final Logger logger = LoggerFactory.getLogger(BukkitChunkGeneratorWrapper.class);
    
    private static final Map<com.dfsek.terra.api.world.World, PopulationManager> popMap = new HashMap<>();
    
    private final PopulationManager popMan;
    
    private final ChunkGenerator delegate;
    
    private boolean needsLoad = true;
    
    private WorldConfig worldConfig;
    
    public BukkitChunkGeneratorWrapper(ChunkGenerator delegate) {
        this.delegate = delegate;
        Platform platform = delegate.getPlatform();
        this.popMan = new PopulationManager(delegate, platform);
    }
    
    
    public static synchronized void saveAll() {
        for(Map.Entry<com.dfsek.terra.api.world.World, PopulationManager> entry : popMap.entrySet()) {
            try {
                entry.getValue().saveBlocks(entry.getKey());
            } catch(IOException e) {
                logger.error("Error occurred while saving population manager", e);
            }
        }
    }
    
    public static synchronized void fixChunk(Chunk chunk) {
        popMap.get(chunk.getWorld()).checkNeighbors(chunk.getX(), chunk.getZ(), chunk.getWorld());
    }
    
    private void load(com.dfsek.terra.api.world.World w) {
        try {
            popMan.loadBlocks(w);
        } catch(FileNotFoundException ignore) {
        
        } catch(IOException | ClassNotFoundException e) {
            logger.error("Error occurred while loading terra world", e);
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
        ChunkData data = createChunkData(world);
        delegate.generateChunkData(bukkitWorld, random, x, z, new BukkitProtoChunk(data));
        return data;
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
