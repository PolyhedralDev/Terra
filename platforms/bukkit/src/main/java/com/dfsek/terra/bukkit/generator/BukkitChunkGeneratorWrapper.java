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

import com.dfsek.terra.api.config.ConfigPack;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.dfsek.terra.api.config.WorldConfig;
import com.dfsek.terra.api.world.generator.ChunkGenerator;
import com.dfsek.terra.api.world.generator.GeneratorWrapper;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class BukkitChunkGeneratorWrapper extends org.bukkit.generator.ChunkGenerator implements GeneratorWrapper {
    private final ChunkGenerator delegate;
    
    private WorldConfig worldConfig;
    
    private final ConfigPack pack;
    
    public BukkitChunkGeneratorWrapper(ChunkGenerator delegate, ConfigPack pack) {
        this.delegate = delegate;
        this.pack = pack;
    }
    
    @Override
    public @Nullable BiomeProvider getDefaultBiomeProvider(@NotNull WorldInfo worldInfo) {
        return new BukkitBiomeProvider(pack.getBiomeProviderBuilder());
    }
    
    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z, @NotNull ChunkData chunkData) {
        if(this.worldConfig == null) {
            this.worldConfig = delegate.getConfigPack().toWorldConfig(BukkitAdapter.adapt(Bukkit.getWorld(worldInfo.getUID())));
        }
        delegate.generateChunkData(worldConfig.getWorld(), random, x, z, new BukkitProtoChunk(chunkData));
    }
    
    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return Collections.singletonList(new BukkitPopulatorWrapper(delegate));
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
