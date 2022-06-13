package com.dfsek.terra.bukkit.nms.v1_19_R1;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep.Carving;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.bukkit.craftbukkit.v1_19_R1.block.data.CraftBlockData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;


public class NMSChunkGeneratorDelegate extends ChunkGenerator {
    private final com.dfsek.terra.api.world.chunk.generation.ChunkGenerator delegate;
    
    private final ChunkGenerator vanilla;
    private final ConfigPack pack;
    
    private final long seed;
    
    public NMSChunkGeneratorDelegate(ChunkGenerator vanilla, ConfigPack pack, NMSBiomeProvider biomeProvider, long seed) {
        super(Registries.structureSet(), Optional.empty(), biomeProvider);
        this.delegate = pack.getGeneratorProvider().newInstance(pack);
        this.vanilla = vanilla;
        this.pack = pack;
        this.seed = seed;
    }

    @Override
    protected @NotNull Codec<? extends ChunkGenerator> codec() {
        return ChunkGenerator.CODEC;
    }
    
    @Override
    public void applyCarvers(@NotNull WorldGenRegion chunkRegion, long seed, @NotNull RandomState noiseConfig, @NotNull BiomeManager world,
                             @NotNull StructureManager structureAccessor, @NotNull ChunkAccess chunk, @NotNull Carving carverStep) {
        // no-op
    }
    
    @Override
    public void buildSurface(@NotNull WorldGenRegion region, @NotNull StructureManager structures, @NotNull RandomState noiseConfig, @NotNull ChunkAccess chunk) {
        // no-op
    }
    
    @Override
    public void applyBiomeDecoration(@NotNull WorldGenLevel world, @NotNull ChunkAccess chunk, @NotNull StructureManager structureAccessor) {
        vanilla.applyBiomeDecoration(world, chunk, structureAccessor);
    }
    
    @Override
    public void spawnOriginalMobs(@NotNull WorldGenRegion region) {
        vanilla.spawnOriginalMobs(region);
    }
    
    @Override
    public int getGenDepth() {
        return vanilla.getGenDepth();
    }
    
    @Override
    public @NotNull CompletableFuture<ChunkAccess> fillFromNoise(@NotNull Executor executor, @NotNull Blender blender, @NotNull RandomState noiseConfig,
                                                                 @NotNull StructureManager structureAccessor, @NotNull ChunkAccess chunk) {
        return vanilla.fillFromNoise(executor, blender, noiseConfig, structureAccessor, chunk);
    }
    
    @Override
    public int getSeaLevel() {
        return vanilla.getSeaLevel();
    }
    
    @Override
    public int getMinY() {
        return vanilla.getMinY();
    }
    
    @Override
    public int getBaseHeight(int x, int z, @NotNull Types heightmap, @NotNull LevelHeightAccessor world, @NotNull RandomState noiseConfig) {
        WorldProperties properties = new NMSWorldProperties(seed, world);
        int y = properties.getMaxHeight();
        BiomeProvider biomeProvider = pack.getBiomeProvider().caching(properties);
        while(y >= getMinY() && !heightmap.isOpaque().test(
                ((CraftBlockData) delegate.getBlock(properties, x, y - 1, z, biomeProvider).getHandle()).getState())) {
            y--;
        }
        return y;
    }
    
    @Override
    public @NotNull NoiseColumn getBaseColumn(int x, int z, @NotNull LevelHeightAccessor world, @NotNull RandomState noiseConfig) {
        BlockState[] array = new BlockState[world.getHeight()];
        WorldProperties properties = new NMSWorldProperties(seed, world);
        BiomeProvider biomeProvider = pack.getBiomeProvider().caching(properties);
        for(int y = properties.getMaxHeight() - 1; y >= properties.getMinHeight(); y--) {
            array[y - properties.getMinHeight()] = ((CraftBlockData) delegate.getBlock(properties, x, y, z, biomeProvider)
                                                                             .getHandle()).getState();
        }
        return new NoiseColumn(getMinY(), array);
    }
    
    @Override
    public void addDebugScreenInfo(@NotNull List<String> text, @NotNull RandomState noiseConfig, @NotNull BlockPos pos) {
    
    }
}
