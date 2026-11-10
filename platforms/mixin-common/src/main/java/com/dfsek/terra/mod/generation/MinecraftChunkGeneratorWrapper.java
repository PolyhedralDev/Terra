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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.dfsek.terra.api.block.state.BlockStateExtended;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.ProtoChunk;
import com.dfsek.terra.api.world.chunk.generation.ProtoWorld;
import com.dfsek.terra.api.world.chunk.generation.stage.Chunkified;
import com.dfsek.terra.api.world.chunk.generation.util.GeneratorWrapper;
import com.dfsek.terra.minecraft.generation.TerraTerrain;
import com.dfsek.terra.mod.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.mod.data.Codecs;
import com.dfsek.terra.mod.mixin.access.StructureManagerAccessor;
import com.dfsek.terra.mod.util.MinecraftAdapter;


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
    public void spawnOriginalMobs(WorldGenRegion region) {
        if(this.settings.mobGeneration()) {
            ChunkPos chunkPos = region.getCenter();
            BlockPos sourcePos = chunkPos.getWorldPosition().atY(region.getMaxY() - 1);
            WorldgenRandom chunkRandom = new WorldgenRandom(new LegacyRandomSource(RandomSupport.generateUniqueSeed()));
            chunkRandom.setDecorationSeed(region.getSeed(), chunkPos.getMinBlockX(), chunkPos.getMinBlockZ());
            NaturalSpawner.spawnMobsForChunkGeneration(region, sourcePos, chunkPos, chunkRandom);
        }
    }

    @Override
    public CompletableFuture<ChunkAccess> buildTerrain(ChunkAccess chunk, Blender blender, RandomState noiseConfig,
                                                       StructureManager structureAccessor, BiomeManager biomeAccess,
                                                       WorldGenRegion carverBiomeRegion, Set<Holder<Biome>> possibleBiomes) {
        return CompletableFuture.supplyAsync(() -> {
            ProtoWorld world = (ProtoWorld) ((StructureManagerAccessor) structureAccessor).getLevel();
            BiomeProvider biomeProvider = pack.getBiomeProvider();
            delegate.generateChunkData((ProtoChunk) chunk, world, biomeProvider, chunk.getPos().x(), chunk.getPos().z());

            TerraTerrain.beard(chunk, structureAccessor, delegate, biomeProvider, world,
                pack.getContext().get(PreLoadCompatibilityOptions.class), MinecraftChunkGeneratorWrapper::vanilla);
            return chunk;
        }, Util.backgroundExecutor());
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
        return TerraTerrain.baseHeight(delegate, pack.getBiomeProvider(), MinecraftAdapter.adapt(height, noiseConfig.seed()), height, x, z,
            heightmap.isOpaque(), MinecraftChunkGeneratorWrapper::vanilla);
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor height, RandomState noiseConfig) {
        return TerraTerrain.baseColumn(delegate, pack.getBiomeProvider(), MinecraftAdapter.adapt(height, noiseConfig.seed()), height, x, z,
            MinecraftChunkGeneratorWrapper::vanilla);
    }

    private static BlockState vanilla(com.dfsek.terra.api.block.state.BlockState state) {
        return (BlockState) (state.isExtended() ? ((BlockStateExtended) state).getState() : state);
    }

    @Override
    public void addDebugScreenInfo(List<String> text, RandomState noiseConfig, BlockPos pos, SamplerContext samplerContext) {
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
