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

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigPack;

import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.bukkit.world.BukkitProtoWorld;

import com.dfsek.terra.bukkit.world.BukkitServerWorld;

import com.dfsek.terra.world.SamplerProviderImpl;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.util.GeneratorWrapper;


public class BukkitChunkGeneratorWrapper extends org.bukkit.generator.ChunkGenerator implements GeneratorWrapper {
    private final ChunkGenerator delegate;
    
    private World world;
    private ServerWorld terraWorld;
    
    private final ConfigPack pack;
    private final Platform platform;
    
    public BukkitChunkGeneratorWrapper(ChunkGenerator delegate, ConfigPack pack, Platform platform) {
        this.delegate = delegate;
        this.pack = pack;
        this.platform = platform;
    }
    
    @Override
    public @Nullable BiomeProvider getDefaultBiomeProvider(@NotNull WorldInfo worldInfo) {
        return new BukkitBiomeProvider(pack.getBiomeProvider());
    }
    
    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z, @NotNull ChunkData chunkData) {
        if(this.world == null) {
            this.world = Bukkit.getWorld(worldInfo.getUID());
            this.terraWorld = new BukkitServerWorld(world);
        }
        delegate.generateChunkData(new BukkitProtoChunk(chunkData), terraWorld, z, x);
    }
    
    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return delegate.getGenerationStages().stream().map(generationStage -> new BlockPopulator() {
            @Override
            public void populate(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z, @NotNull LimitedRegion limitedRegion) {
                generationStage.populate(new BukkitProtoWorld(limitedRegion));
            }
        }).collect(Collectors.toList());
    }
    
    @Override
    public boolean shouldGenerateCaves() {
        return pack.vanillaCaves();
    }
    
    @Override
    public boolean shouldGenerateDecorations() {
        return pack.vanillaFlora();
    }
    
    @Override
    public boolean shouldGenerateMobs() {
        return pack.vanillaMobs();
    }
    
    @Override
    public boolean shouldGenerateStructures() {
        return pack.vanillaStructures();
    }
    
    
    public World getWorld() {
        return world;
    }
    
    public ConfigPack getPack() {
        return pack;
    }
    
    @Override
    public ChunkGenerator getHandle() {
        return delegate;
    }
}
