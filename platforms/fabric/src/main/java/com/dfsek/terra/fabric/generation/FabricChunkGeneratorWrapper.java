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

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.generator.ChunkData;
import com.dfsek.terra.api.world.generator.ChunkGenerator;
import com.dfsek.terra.api.world.generator.Chunkified;
import com.dfsek.terra.api.world.generator.GeneratorWrapper;
import com.dfsek.terra.fabric.FabricEntryPoint;
import com.dfsek.terra.fabric.block.FabricBlockState;
import com.dfsek.terra.fabric.mixin.StructureAccessorAccessor;
import com.dfsek.terra.util.FastRandom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.class_6748;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.MultiNoiseSampler;
import net.minecraft.world.biome.source.util.MultiNoiseUtil.NoiseValuePoint;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep.Carver;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.*;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


public class FabricChunkGeneratorWrapper extends net.minecraft.world.gen.chunk.ChunkGenerator implements GeneratorWrapper {
    public static final Codec<ConfigPack> PACK_CODEC = RecordCodecBuilder.create(
            config -> config.group(
                    Codec.STRING.fieldOf("pack")
                                .forGetter(ConfigPack::getID)
                                  ).apply(config, config.stable(FabricEntryPoint.getPlatform().getConfigRegistry()::get)));
    
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
    private ServerWorld world;
    
    public FabricChunkGeneratorWrapper(TerraBiomeSource biomeSource, long seed, ConfigPack configPack) {
        super(biomeSource, new StructuresConfig(false));
        this.pack = configPack;
        
        this.delegate = pack.getGeneratorProvider().newInstance(pack);
        delegate.getPlatform().logger().info("Loading world with config pack " + pack.getID());
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
    
    @Override
    public MultiNoiseSampler getMultiNoiseSampler() {
        return (x, y, z) -> new NoiseValuePoint(0, 0, 0, 0, 0, 0);
    }
    
    @Override
    public void carve(ChunkRegion chunkRegion, long seed, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk,
                      Carver generationStep) {
        
    }
    
    
    @Nullable
    @Override
    public BlockPos locateStructure(ServerWorld world, StructureFeature<?> feature, BlockPos center, int radius,
                                    boolean skipExistingChunks) {
        /*
        if(!pack.disableStructures()) {
            String name = Objects.requireNonNull(Registry.STRUCTURE_FEATURE.getId(feature)).toString();
            TerraWorld terraWorld = TerraFabricPlugin.getInstance().getWorld((World) world);
            ConfiguredStructure located = pack.getRegistry(TerraStructure.class).get(pack.getLocatable().get(name));
            if(located != null) {
                CompletableFuture<BlockPos> result = new CompletableFuture<>();
                AsyncStructureFinder finder = new AsyncStructureFinder(terraWorld.getBiomeProvider(), located, FabricAdapter.adapt
                (center), terraWorld.getWorld(), 0, 500, location -> {
                    result.complete(FabricAdapter.adapt(location));
                }, TerraFabricPlugin.getInstance());
                finder.run(); // Do this synchronously.
                try {
                    return result.get();
                } catch(InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
         */
        return super.locateStructure(world, feature, center, radius, skipExistingChunks);
    }
    
    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, Chunk chunk) {
        // no op
    }
    
    @Override
    public void populateEntities(ChunkRegion region) {
        if(pack.vanillaMobs()) {
            int cx = region.getCenterPos().x;
            int cy = region.getCenterPos().z;
            Biome biome = region.getBiome((new ChunkPos(cx, cy)).getStartPos());
            SpawnHelper.populateEntities(region, biome, region.getCenterPos(), region.getRandom());
        }
    }
    
    @Override
    public int getWorldHeight() {
        return 256; //fixme
    }
    
    public Pool<SpawnSettings.SpawnEntry> getEntitySpawnList(Biome biome, StructureAccessor accessor, SpawnGroup group, BlockPos pos) {
        if(!accessor.hasStructureReferences(pos)) {
            return super.getEntitySpawnList(biome, accessor, group, pos);
        } else {
            if(accessor.method_38854(pos, StructureFeature.SWAMP_HUT).hasChildren()) {
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
                
                if(accessor.method_38854(pos, StructureFeature.FORTRESS).hasChildren()) {
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
    public CompletableFuture<Chunk> populateNoise(Executor executor, class_6748 arg, StructureAccessor structureAccessor, Chunk chunk) {
        return CompletableFuture.supplyAsync(() -> {
            World world = (World) ((StructureAccessorAccessor) structureAccessor).getWorld();
            delegate.generateChunkData(world, new FastRandom(), chunk.getPos().x, chunk.getPos().z, (ChunkData) chunk);
            delegate.getGenerationStages().forEach(populator -> {
                if(populator instanceof Chunkified) {
                    populator.populate(world, (com.dfsek.terra.api.world.Chunk) world);
                }
            });
            return chunk;
        }, executor);
    }
    
    @Override
    public int getSeaLevel() {
        return 0; //fixme
    }
    
    @Override
    public int getMinimumY() {
        return 0; //fixmw
    }
    
    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView heightmapType) {
        int height = ((World) world).getMaxHeight();
        while(height >= ((World) world).getMinHeight() && !heightmap.getBlockPredicate().test(
                ((FabricBlockState) ((World) world).getGenerator().getBlock((World) world, x, height - 1, z)).getHandle())) {
            height--;
        }
        return height;
    }
    
    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView view) {
        BlockState[] array = new BlockState[view.getHeight()];
        for(int y = view.getBottomY() + view.getHeight() - 1; y >= view.getBottomY(); y--) {
            array[y] = ((FabricBlockState) ((World) world).getGenerator().getBlock((World) world, x, y, z)).getHandle();
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
    
    public void setPack(ConfigPack pack) {
        this.pack = pack;
        this.delegate = pack.getGeneratorProvider().newInstance(pack);
        biomeSource.setPack(pack);
        
        delegate.getPlatform().logger().info("Loading world with config pack " + pack.getID());
    }
    
    public void setWorld(ServerWorld world) {
        this.world = world;
    }
    
    @Override
    public ChunkGenerator getHandle() {
        return delegate;
    }
}
