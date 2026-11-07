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

package com.dfsek.terra.mod.generation;

import com.mojang.serialization.MapCodec;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Util;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction.SinglePointContext;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.dfsek.terra.api.block.state.BlockStateExtended;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.ProtoChunk;
import com.dfsek.terra.api.world.chunk.generation.ProtoWorld;
import com.dfsek.terra.api.world.chunk.generation.stage.Chunkified;
import com.dfsek.terra.api.world.chunk.generation.util.GeneratorWrapper;
import com.dfsek.terra.api.world.info.WorldProperties;
import com.dfsek.terra.mod.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.mod.data.Codecs;
import com.dfsek.terra.mod.mixin.access.StructureManagerAccessor;
import com.dfsek.terra.mod.util.MinecraftAdapter;
import com.dfsek.terra.mod.util.SeedHack;


public class MinecraftChunkGeneratorWrapper extends net.minecraft.world.level.chunk.ChunkGenerator implements GeneratorWrapper {
    private static final Logger logger = LoggerFactory.getLogger(MinecraftChunkGeneratorWrapper.class);

    private final TerraBiomeSource biomeSource;
    private final GenerationSettings settings;
    private ChunkGenerator delegate;
    private ConfigPack pack;


    public MinecraftChunkGeneratorWrapper(TerraBiomeSource biomeSource, ConfigPack configPack,
                                          GenerationSettings settingsSupplier) {
        super(biomeSource);
        this.pack = configPack;
        this.settings = settingsSupplier;

        this.delegate = pack.getGeneratorProvider().newInstance(pack);
        logger.info("Loading world with config pack {}", pack.getID());
        this.biomeSource = biomeSource;
    }

    @Override
    protected MapCodec<? extends net.minecraft.world.level.chunk.ChunkGenerator> codec() {
        return Codecs.MINECRAFT_CHUNK_GENERATOR_WRAPPER;
    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structures, RandomState noiseConfig, ChunkAccess chunk) {
        // no op
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
        if(this.settings.mobGeneration()) {
            ChunkPos chunkPos = region.getCenter();
            Holder<Biome> registryEntry = region.getBiome(chunkPos.getWorldPosition().atY(region.getMaxY() - 1));
            WorldgenRandom chunkRandom = new WorldgenRandom(new LegacyRandomSource(RandomSupport.generateUniqueSeed()));
            chunkRandom.setDecorationSeed(region.getSeed(), chunkPos.getMinBlockX(), chunkPos.getMinBlockZ());
            NaturalSpawner.spawnMobsForChunkGeneration(region, registryEntry, chunkPos, chunkRandom);
        }
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState noiseConfig, StructureManager structureAccessor,
                                                  ChunkAccess chunk) {
        return CompletableFuture.supplyAsync(() -> {
            ProtoWorld world = (ProtoWorld) ((StructureManagerAccessor) structureAccessor).getLevel();
            BiomeProvider biomeProvider = pack.getBiomeProvider();
            delegate.generateChunkData((ProtoChunk) chunk, world, biomeProvider, chunk.getPos().x, chunk.getPos().z);

            PreLoadCompatibilityOptions compatibilityOptions = pack.getContext().get(PreLoadCompatibilityOptions.class);
            if(compatibilityOptions.isBeard()) {
                beard(structureAccessor, chunk, world, biomeProvider, compatibilityOptions);
            }
            return chunk;
        }, Util.backgroundExecutor());
    }

    private void beard(StructureManager structureAccessor, ChunkAccess chunk, WorldProperties world, BiomeProvider biomeProvider,
                       PreLoadCompatibilityOptions compatibilityOptions) {
        Beardifier structureWeightSampler = Beardifier.forStructuresInChunk(structureAccessor,
            chunk.getPos());
        double threshold = compatibilityOptions.getBeardThreshold();
        double airThreshold = compatibilityOptions.getAirThreshold();
        int xi = chunk.getPos().x << 4;
        int zi = chunk.getPos().z << 4;
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                int depth = 0;
                for(int y = world.getMaxHeight(); y >= world.getMinHeight(); y--) {
                    double noise = structureWeightSampler.compute(new SinglePointContext(x + xi, y, z + zi));
                    if(noise > threshold) {
                        com.dfsek.terra.api.block.state.BlockState data = delegate.getPalette(x + xi, y, z + zi, world, biomeProvider).get(
                            depth, x + xi, y, z + zi, world.getSeed());
                        BlockPos blockPos = new BlockPos(x, y, z);
                        boolean isExtended = data.isExtended() && data.getClass().equals(BlockInput.class);
                        if(isExtended) {
                            BlockStateExtended blockStateExtended = (BlockStateExtended) data;

                            net.minecraft.world.level.block.state.BlockState blockState = (net.minecraft.world.level.block.state.BlockState) blockStateExtended.getState();
                            chunk.setBlockState(blockPos, blockState, 0);
                        } else {
                            chunk.setBlockState(blockPos, (net.minecraft.world.level.block.state.BlockState) data, 0);
                        }
                        depth++;
                    } else if(noise < airThreshold) {
                        chunk.setBlockState(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 0);
                    } else {
                        depth = 0;
                    }
                }
            }
        }
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel world, ChunkAccess chunk, StructureManager structureAccessor) {
        super.applyBiomeDecoration(world, chunk, structureAccessor);
        pack.getStages().forEach(populator -> {
            if(!(populator instanceof Chunkified)) {
                populator.populate((ProtoWorld) world);
            }
        });
    }

    @Override
    public int getGenDepth() {
        return settings.height().getRange();
    }

    @Override
    public int getSeaLevel() {
        return settings.sealevel();
    }

    @Override
    public int getMinY() {
        return settings.height().getMin();
    }

    @Override
    public int getSpawnHeight(LevelHeightAccessor world) {
        return settings.spawnHeight();
    }

    @Override
    public int getBaseHeight(int x, int z, Types heightmap, LevelHeightAccessor height, RandomState noiseConfig) {
        WorldProperties properties = MinecraftAdapter.adapt(height, SeedHack.getSeed(noiseConfig.sampler()));
        BiomeProvider biomeProvider = pack.getBiomeProvider();
        int min = height.getMinY();
        for(int y = height.getMaxY() - 1; y >= min; y--) {
            com.dfsek.terra.api.block.state.BlockState terraBlockState = delegate.getBlock(properties, x, y, z, biomeProvider);
            BlockState blockState =
                (BlockState) (terraBlockState.isExtended() ? ((BlockStateExtended) terraBlockState).getState() : terraBlockState);
            if(heightmap
                .isOpaque()
                .test(blockState)) return y + 1;
        }
        return min;
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor height, RandomState noiseConfig) {
        BlockState[] array = new BlockState[height.getHeight()];
        WorldProperties properties = MinecraftAdapter.adapt(height, SeedHack.getSeed(noiseConfig.sampler()));
        BiomeProvider biomeProvider = pack.getBiomeProvider();
        for(int y = height.getMaxY() - 1; y >= height.getMinY(); y--) {
            com.dfsek.terra.api.block.state.BlockState terraBlockState = delegate.getBlock(properties, x, y, z, biomeProvider);
            BlockState blockState =
                (BlockState) (terraBlockState.isExtended() ? ((BlockStateExtended) terraBlockState).getState() : terraBlockState);
            array[y - height.getMinY()] = blockState;
        }
        return new NoiseColumn(height.getMinY(), array);
    }

    @Override
    public void addDebugScreenInfo(List<String> text, RandomState noiseConfig, BlockPos pos) {
        // no op
    }

    public ConfigPack getPack() {
        return pack;
    }

    public void setPack(ConfigPack pack) {
        this.pack = pack;
        this.delegate = pack.getGeneratorProvider().newInstance(pack);
        biomeSource.setPack(pack);

        logger.debug("Loading world with config pack {}", pack.getID());
    }


    @Override
    public void applyCarvers(WorldGenRegion chunkRegion, long seed, RandomState noiseConfig, BiomeManager biomeAccess,
                      StructureManager structureAccessor, ChunkAccess chunk) {
        //no op
    }

    @Override
    public ChunkGenerator getHandle() {
        return delegate;
    }

    public GenerationSettings getSettings() {
        return settings;
    }

    @Override
    public TerraBiomeSource getBiomeSource() {
        return biomeSource;
    }
}
