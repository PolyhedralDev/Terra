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

import org.bukkit.HeightMap;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.util.GeneratorWrapper;
import com.dfsek.terra.bukkit.util.BukkitUtils;
import com.dfsek.terra.bukkit.world.BukkitWorldProperties;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockState;


public class BukkitChunkGeneratorWrapper extends org.bukkit.generator.ChunkGenerator implements GeneratorWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(BukkitChunkGeneratorWrapper.class);
    private final BlockState air;
    private final BukkitBlockPopulator blockPopulator;
    private ChunkGenerator delegate;
    private ConfigPack pack;


    public BukkitChunkGeneratorWrapper(ChunkGenerator delegate, ConfigPack pack, BlockState air) {
        this.delegate = delegate;
        this.pack = pack;
        this.air = air;
        this.blockPopulator = new BukkitBlockPopulator(pack, air);
    }

    public void setDelegate(ChunkGenerator delegate) {
        this.delegate = delegate;
    }

    @Override
    public @Nullable BiomeProvider getDefaultBiomeProvider(@NotNull WorldInfo worldInfo) {
        return new BukkitBiomeProvider(pack.getBiomeProvider());
    }

    @Override
    public void generateNoise(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z, @NotNull ChunkData chunkData) {
        BukkitWorldProperties properties = new BukkitWorldProperties(worldInfo);
        delegate.generateChunkData(new BukkitProtoChunk(chunkData), properties, pack.getBiomeProvider(), x, z);
    }

    @Override
    public int getBaseHeight(@NotNull WorldInfo worldInfo, @NotNull Random random, int x, int z, @NotNull HeightMap heightMap) {
        BukkitWorldProperties properties = new BukkitWorldProperties(worldInfo);
        com.dfsek.terra.api.world.biome.generation.BiomeProvider biomeProvider = pack.getBiomeProvider();
        int y = properties.getMaxHeight();
        while(y > properties.getMinHeight() &&
              !isHeightmapBlock(heightMap, ((BukkitBlockState) delegate.getBlock(properties, x, y - 1, z, biomeProvider)).getHandle())) {
            y--;
        }
        return y;
    }

    private static boolean isHeightmapBlock(HeightMap heightMap, BlockData data) {
        Material material = data.getMaterial();
        return switch(heightMap) {
            case WORLD_SURFACE, WORLD_SURFACE_WG -> !material.isAir();
            case OCEAN_FLOOR, OCEAN_FLOOR_WG -> material.isSolid();
            case MOTION_BLOCKING -> material.isSolid() || hasFluid(data);
            case MOTION_BLOCKING_NO_LEAVES -> (material.isSolid() || hasFluid(data)) && !Tag.LEAVES.isTagged(material);
        };
    }

    private static boolean hasFluid(BlockData data) {
        return BukkitUtils.isLiquid(data) || (data instanceof Waterlogged waterlogged && waterlogged.isWaterlogged());
    }

    @Override
    public @NotNull List<BlockPopulator> getDefaultPopulators(@NotNull World world) {
        return List.of(blockPopulator);
    }

    @Override
    public boolean shouldGenerateCaves() {
        return false;
        //return pack.vanillaCaves();
    }

    @Override
    public boolean shouldGenerateDecorations() {
        return true;
    }

    @Override
    public boolean shouldGenerateMobs() {
        return true;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return true;
    }

    public ConfigPack getPack() {
        return pack;
    }

    public void setPack(ConfigPack pack) {
        this.pack = pack;
        setDelegate(pack.getGeneratorProvider().newInstance(pack));
    }

    @Override
    public ChunkGenerator getHandle() {
        return delegate;
    }
}
