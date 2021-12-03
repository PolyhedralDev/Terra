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

package com.dfsek.terra.fabric.generation;

import com.dfsek.terra.api.world.ServerWorld;
import com.dfsek.terra.api.world.chunk.generation.ProtoWorld;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.NetherFortressFeature;
import net.minecraft.world.gen.feature.OceanMonumentFeature;
import net.minecraft.world.gen.feature.PillagerOutpostFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.SwampHutFeature;
import net.minecraft.world.gen.random.AtomicSimpleRandom;
import net.minecraft.world.gen.random.ChunkRandom;
import net.minecraft.world.gen.random.RandomSeed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.chunk.generation.ProtoChunk;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.chunk.generation.stage.Chunkified;
import com.dfsek.terra.api.world.chunk.generation.util.GeneratorWrapper;
import com.dfsek.terra.fabric.FabricEntryPoint;
import com.dfsek.terra.fabric.block.FabricBlockState;
import com.dfsek.terra.fabric.mixin.access.StructureAccessorAccessor;


public class FabricChunkGeneratorWrapper extends net.minecraft.world.gen.chunk.ChunkGenerator implements GeneratorWrapper {
    private static final Logger logger = LoggerFactory.getLogger(FabricChunkGeneratorWrapper.class);
    
    public static final Codec<ConfigPack> PACK_CODEC = RecordCodecBuilder.create(
            config -> config.group(
                    Codec.STRING.fieldOf("pack")
                                .forGetter(ConfigPack::getID)
                                  ).apply(config, config.stable(id -> FabricEntryPoint.getPlatform()
                                                                                      .getConfigRegistry()
                                                                                      .get(id)
                                                                                      .orElseThrow(
                                                                                              () -> new IllegalArgumentException(
                                                                                                      "No such config pack " +
                                                                                                      id)))));
    
    public static final Codec<FabricChunkGeneratorWrapper> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    TerraBiomeSource.CODEC.fieldOf("biome_source")
                                          .forGetter(generator -> generator.biomeSource),
                    Codec.LONG.fieldOf("seed").stable()
                              .forGetter(generator -> generator.seed),
                    PACK_CODEC.fieldOf("pack").stable()
                              .forGetter(generator -> generator.pack)
                                      ).apply(instance, instance.stable(FabricChunkGeneratorWrapper::new))
                                                                                            );
    
    private final long seed;
    private final TerraBiomeSource biomeSource;
    private ChunkGenerator delegate;
    private ConfigPack pack;
    private net.minecraft.server.world.ServerWorld world;
    
    public FabricChunkGeneratorWrapper(TerraBiomeSource biomeSource, long seed, ConfigPack configPack) {
        super(biomeSource, new StructuresConfig(false));
        this.pack = configPack;
        
        this.delegate = pack.getGeneratorProvider().newInstance(pack);
        logger.info("Loading world with config pack {}", pack.getID());
        this.biomeSource = biomeSource;
        
        this.seed = seed;
    }
    
    @Override
    protected Codec<? extends net.minecraft.world.gen.chunk.ChunkGenerator> getCodec() {
        return CODEC;
    }
    
    @Override
    public net.minecraft.world.gen.chunk.ChunkGenerator withSeed(long seed) {
        return new FabricChunkGeneratorWrapper((TerraBiomeSource) this.biomeSource.withSeed(seed), seed, pack);
    }
    
    public void setPack(ConfigPack pack) {
        this.pack = pack;
        this.delegate = pack.getGeneratorProvider().newInstance(pack);
        biomeSource.setPack(pack);
        
        logger.debug("Loading world with config pack {}", pack.getID());
    }
    
    @Override
    public MultiNoiseUtil.MultiNoiseSampler getMultiNoiseSampler() {
        return (x, y, z) -> new MultiNoiseUtil.NoiseValuePoint(0, 0, 0, 0, 0, 0);
    }
    
    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, Chunk chunk) {
        // no op
    }
    
    @Override
    public void populateEntities(ChunkRegion region) {
        if(pack.vanillaMobs()) {
            ChunkPos chunkPos = region.getCenterPos();
            Biome biome = region.getBiome(chunkPos.getStartPos().withY(region.getTopY() - 1));
            ChunkRandom chunkRandom = new ChunkRandom(new AtomicSimpleRandom(RandomSeed.getSeed()));
            chunkRandom.setPopulationSeed(region.getSeed(), chunkPos.getStartX(), chunkPos.getStartZ());
            SpawnHelper.populateEntities(region, biome, chunkPos, chunkRandom);
        }
    }
    
    @Override
    public int getWorldHeight() {
        return world.getTopY();
    }
    
    public Pool<SpawnSettings.SpawnEntry> getEntitySpawnList(Biome biome, StructureAccessor accessor, SpawnGroup group, BlockPos pos) {
        if(!accessor.hasStructureReferences(pos)) {
            return super.getEntitySpawnList(biome, accessor, group, pos);
        } else {
            if(accessor.getStructureAt(pos, StructureFeature.SWAMP_HUT).hasChildren()) {
                if(group == SpawnGroup.MONSTER) {
                    return SwampHutFeature.MONSTER_SPAWNS;
                }
                
                if(group == SpawnGroup.CREATURE) {
                    return SwampHutFeature.CREATURE_SPAWNS;
                }
            }
            
            if(group == SpawnGroup.MONSTER) {
                if(accessor.getStructureAt(pos, StructureFeature.PILLAGER_OUTPOST).hasChildren()) {
                    return PillagerOutpostFeature.MONSTER_SPAWNS;
                }
                
                if(accessor.getStructureAt(pos, StructureFeature.MONUMENT).hasChildren()) {
                    return OceanMonumentFeature.MONSTER_SPAWNS;
                }
                
                if(accessor.getStructureAt(pos, StructureFeature.FORTRESS).hasChildren()) {
                    return NetherFortressFeature.MONSTER_SPAWNS;
                }
            }
            
            return (group == SpawnGroup.UNDERGROUND_WATER_CREATURE || group == SpawnGroup.AXOLOTLS) && accessor.getStructureAt(pos,
                                                                                                                               StructureFeature.MONUMENT)
                                                                                                               .hasChildren()
                   ? SpawnSettings.EMPTY_ENTRY_POOL
                   : super.getEntitySpawnList(biome, accessor, group, pos);
        }
    }
    
    @Override
    public void setStructureStarts(DynamicRegistryManager dynamicRegistryManager, StructureAccessor structureAccessor, Chunk chunk,
                                   StructureManager structureManager, long worldSeed) {
        if(pack.vanillaStructures()) {
            super.setStructureStarts(dynamicRegistryManager, structureAccessor, chunk, structureManager, worldSeed);
        }
    }
    
    @Override
    public CompletableFuture<Chunk> populateNoise(Executor executor, Blender arg, StructureAccessor structureAccessor, Chunk chunk) {
        return CompletableFuture.supplyAsync(() -> {
            ProtoWorld world = (ProtoWorld) ((StructureAccessorAccessor) structureAccessor).getWorld();
            delegate.generateChunkData((ProtoChunk) chunk, world, chunk.getPos().z, chunk.getPos().x);
            delegate.getGenerationStages().forEach(populator -> {
                if(populator instanceof Chunkified) {
                    populator.populate(world);
                }
            });
            return chunk;
        }, executor);
    }
    
    @Override
    public void generateFeatures(StructureWorldAccess world, Chunk chunk, StructureAccessor structureAccessor) {
        delegate.getGenerationStages().forEach(populator -> {
            if(!(populator instanceof Chunkified)) {
                populator.populate((ProtoWorld) world);
            }
        });
        if(pack.vanillaFlora()) {
            super.generateFeatures(world, chunk, structureAccessor);
        }
    }
    
    @Override
    public int getSeaLevel() {
        return world.getSeaLevel();
    }
    
    @Override
    public int getMinimumY() {
        return world.getBottomY();
    }
    
    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView heightmapType) {
        int height = ((ServerWorld) world).getMaxHeight();
        while(height >= ((ServerWorld) world).getMinHeight() && !heightmap.getBlockPredicate().test(
                ((FabricBlockState) ((ServerWorld) world).getGenerator().getBlock((ServerWorld) world, x, height - 1, z)).getHandle())) {
            height--;
        }
        return height;
    }
    
    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView view) {
        BlockState[] array = new BlockState[view.getHeight()];
        for(int y = view.getBottomY() + view.getHeight() - 1; y >= view.getBottomY(); y--) {
            array[y] = ((FabricBlockState) ((ServerWorld) world).getGenerator().getBlock((ServerWorld) world, x, y, z)).getHandle();
        }
        return new VerticalBlockSample(view.getBottomY(), array);
    }
    
    @Override
    public int getHeightOnGround(int x, int z, Heightmap.Type heightmap, HeightLimitView world) {
        return super.getHeightOnGround(x, z, heightmap, world);
    }
    
    @Override
    public boolean isStrongholdStartingChunk(ChunkPos chunkPos) {
        if(pack.vanillaStructures()) {
            return super.isStrongholdStartingChunk(chunkPos);
        }
        return false;
    }
    
    public ConfigPack getPack() {
        return pack;
    }
    
    @Override
    public void carve(ChunkRegion chunkRegion, long seed, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk,
                      GenerationStep.Carver generationStep) {
        
    }
    
    public void setWorld(net.minecraft.server.world.ServerWorld world) {
        this.world = world;
    }
    
    @Override
    public ChunkGenerator getHandle() {
        return delegate;
    }
}
