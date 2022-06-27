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

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.RandomSeed;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep.Carver;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.StructureWeightSampler;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.densityfunction.DensityFunction.UnblendedNoisePos;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

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
import com.dfsek.terra.mod.mixin.access.StructureAccessorAccessor;
import com.dfsek.terra.mod.util.MinecraftAdapter;


public class MinecraftChunkGeneratorWrapper extends net.minecraft.world.gen.chunk.ChunkGenerator implements GeneratorWrapper {
    private static final Logger logger = LoggerFactory.getLogger(MinecraftChunkGeneratorWrapper.class);
    
    private final TerraBiomeSource biomeSource;
    private final Registry<StructureSet> noiseRegistry;
    private final RegistryEntry<ChunkGeneratorSettings> settings;
    private ChunkGenerator delegate;
    private ConfigPack pack;
    
    public MinecraftChunkGeneratorWrapper(Registry<StructureSet> noiseRegistry, TerraBiomeSource biomeSource, ConfigPack configPack,
                                          RegistryEntry<ChunkGeneratorSettings> settingsSupplier) {
        super(noiseRegistry, Optional.empty(), biomeSource);
        this.noiseRegistry = noiseRegistry;
        this.pack = configPack;
        this.settings = settingsSupplier;
        
        this.delegate = pack.getGeneratorProvider().newInstance(pack);
        logger.info("Loading world with config pack {}", pack.getID());
        this.biomeSource = biomeSource;
    }
    
    public Registry<StructureSet> getNoiseRegistry() {
        return noiseRegistry;
    }
    
    @Override
    protected Codec<? extends net.minecraft.world.gen.chunk.ChunkGenerator> getCodec() {
        return Codecs.MINECRAFT_CHUNK_GENERATOR_WRAPPER;
    }
    
    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {
        // no op
    }
    
    @Override
    public void populateEntities(ChunkRegion region) {
        if(!this.settings.value().mobGenerationDisabled()) {
            ChunkPos chunkPos = region.getCenterPos();
            RegistryEntry<Biome> registryEntry = region.getBiome(chunkPos.getStartPos().withY(region.getTopY() - 1));
            ChunkRandom chunkRandom = new ChunkRandom(new CheckedRandom(RandomSeed.getSeed()));
            chunkRandom.setPopulationSeed(region.getSeed(), chunkPos.getStartX(), chunkPos.getStartZ());
            SpawnHelper.populateEntities(region, registryEntry, chunkPos, chunkRandom);
        }
    }
    
    @Override
    public int getWorldHeight() {
        return settings.value().generationShapeConfig().height();
    }
    
    
    @Override
    public CompletableFuture<Chunk> populateNoise(Executor executor, Blender blender, NoiseConfig noiseConfig,
                                                  StructureAccessor structureAccessor, Chunk chunk) {
        return CompletableFuture.supplyAsync(() -> {
            ProtoWorld world = (ProtoWorld) ((StructureAccessorAccessor) structureAccessor).getWorld();
            BiomeProvider biomeProvider = pack.getBiomeProvider();
            delegate.generateChunkData((ProtoChunk) chunk, world, biomeProvider, chunk.getPos().x, chunk.getPos().z);
            
            PreLoadCompatibilityOptions compatibilityOptions = pack.getContext().get(PreLoadCompatibilityOptions.class);
            if(compatibilityOptions.isBeard()) {
                beard(structureAccessor, chunk, world, biomeProvider, compatibilityOptions);
            }
            return chunk;
        }, Util.getMainWorkerExecutor());
    }
    
    private void beard(StructureAccessor structureAccessor, Chunk chunk, WorldProperties world, BiomeProvider biomeProvider,
                       PreLoadCompatibilityOptions compatibilityOptions) {
        StructureWeightSampler structureWeightSampler = StructureWeightSampler.method_42695(structureAccessor, chunk.getPos());
        double threshold = compatibilityOptions.getBeardThreshold();
        double airThreshold = compatibilityOptions.getAirThreshold();
        int xi = chunk.getPos().x << 4;
        int zi = chunk.getPos().z << 4;
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                int depth = 0;
                for(int y = world.getMaxHeight(); y >= world.getMinHeight(); y--) {
                    double noise = structureWeightSampler.sample(new UnblendedNoisePos(x + xi, y, z + zi));
                    if(noise > threshold) {
                        chunk.setBlockState(new BlockPos(x, y, z), (BlockState) delegate
                                .getPalette(x + xi, y, z + zi, world, biomeProvider)
                                .get(depth, x + xi, y, z + zi, world.getSeed()), false);
                        depth++;
                    } else if(noise < airThreshold) {
                        chunk.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState(), false);
                    } else {
                        depth = 0;
                    }
                }
            }
        }
    }
    
    @Override
    public void generateFeatures(StructureWorldAccess world, Chunk chunk, StructureAccessor structureAccessor) {
        super.generateFeatures(world, chunk, structureAccessor);
        pack.getStages().forEach(populator -> {
            if(!(populator instanceof Chunkified)) {
                populator.populate((ProtoWorld) world);
            }
        });
    }
    
    @Override
    public int getSeaLevel() {
        return settings.value().seaLevel();
    }
    
    @Override
    public int getMinimumY() {
        return settings.value().generationShapeConfig().minimumY();
    }
    
    
    @Override
    public int getHeight(int x, int z, Type heightmap, HeightLimitView height, NoiseConfig noiseConfig) {
        WorldProperties properties = MinecraftAdapter.adapt(height, noiseConfig.getLegacyWorldSeed());
        BiomeProvider biomeProvider = pack.getBiomeProvider();
        int min = height.getBottomY();
        for(int y = height.getTopY() - 1; y >= min; y--) {
            if(heightmap
                    .getBlockPredicate()
                    .test((BlockState) delegate.getBlock(properties, x, y, z, biomeProvider))) return y + 1;
        }
        return min;
    }
    
    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView height, NoiseConfig noiseConfig) {
        BlockState[] array = new BlockState[height.getHeight()];
        WorldProperties properties = MinecraftAdapter.adapt(height, noiseConfig.getLegacyWorldSeed());
        BiomeProvider biomeProvider = pack.getBiomeProvider();
        for(int y = height.getTopY() - 1; y >= height.getBottomY(); y--) {
            array[y - height.getBottomY()] = (BlockState) delegate.getBlock(properties, x, y, z, biomeProvider);
        }
        return new VerticalBlockSample(height.getBottomY(), array);
    }
    
    @Override
    public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {
    
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
    public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess world, StructureAccessor structureAccessor,
                      Chunk chunk, Carver carverStep) {
        // no op
    }
    
    @Override
    public ChunkGenerator getHandle() {
        return delegate;
    }
    
    public RegistryEntry<ChunkGeneratorSettings> getSettings() {
        return settings;
    }
    
    @Override
    public TerraBiomeSource getBiomeSource() {
        return biomeSource;
    }
}
