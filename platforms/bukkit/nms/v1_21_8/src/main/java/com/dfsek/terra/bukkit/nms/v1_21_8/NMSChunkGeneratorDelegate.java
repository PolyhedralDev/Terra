package com.dfsek.terra.bukkit.nms.v1_21_8;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction.SinglePointContext;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;
import com.dfsek.terra.bukkit.config.PreLoadCompatibilityOptions;
import com.dfsek.terra.bukkit.world.BukkitWorldProperties;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockState;


public class NMSChunkGeneratorDelegate extends ChunkGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(NMSChunkGeneratorDelegate.class);
    private final com.dfsek.terra.api.world.chunk.generation.ChunkGenerator delegate;

    private final ChunkGenerator vanilla;
    private final ConfigPack pack;

    private final long seed;

    public NMSChunkGeneratorDelegate(ChunkGenerator vanilla, ConfigPack pack, NMSBiomeProvider biomeProvider, long seed) {
        super(biomeProvider);
        this.delegate = pack.getGeneratorProvider().newInstance(pack);
        this.vanilla = vanilla;
        this.pack = pack;
        this.seed = seed;
    }

    @Override
    protected @NotNull MapCodec<? extends ChunkGenerator> codec() {
        return MapCodec.assumeMapUnsafe(ChunkGenerator.CODEC);
    }

    @Override
    public void applyCarvers(@NotNull WorldGenRegion chunkRegion, long seed, @NotNull RandomState noiseConfig, @NotNull BiomeManager world,
                             @NotNull StructureManager structureAccessor, @NotNull ChunkAccess chunk) {
        // no-op
    }

    @Override
    public void buildSurface(@NotNull WorldGenRegion region, @NotNull StructureManager structures, @NotNull RandomState noiseConfig,
                             @NotNull ChunkAccess chunk) {
        // no-op
    }

    @Override
    public void applyBiomeDecoration(@NotNull WorldGenLevel world, @NotNull ChunkAccess chunk,
                                     @NotNull StructureManager structureAccessor) {
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
    public CompletableFuture<ChunkAccess> fillFromNoise(@NotNull Blender blender,
                                                        @NotNull RandomState noiseConfig,
                                                        @NotNull StructureManager structureAccessor, @NotNull ChunkAccess chunk) {
        return vanilla.fillFromNoise(blender, noiseConfig, structureAccessor, chunk)
            .thenApply(c -> {
                LevelAccessor level = Reflection.STRUCTURE_MANAGER.getLevel(structureAccessor);
                BiomeProvider biomeProvider = pack.getBiomeProvider();
                PreLoadCompatibilityOptions compatibilityOptions = pack.getContext().get(PreLoadCompatibilityOptions.class);
                if(compatibilityOptions.isBeard()) {
                    beard(structureAccessor, chunk, new BukkitWorldProperties(level.getMinecraftWorld().getWorld()),
                        biomeProvider, compatibilityOptions);
                }
                return c;
            });
    }

    private void beard(StructureManager structureAccessor, ChunkAccess chunk, WorldProperties world, BiomeProvider biomeProvider,
                       PreLoadCompatibilityOptions compatibilityOptions) {
        Beardifier structureWeightSampler = Beardifier.forStructuresInChunk(structureAccessor, chunk.getPos());
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
                        chunk.setBlockState(new BlockPos(x, y, z), ((CraftBlockData) ((BukkitBlockState) delegate
                            .getPalette(x + xi, y, z + zi, world, biomeProvider)
                            .get(depth, x + xi, y, z + zi, world.getSeed())).getHandle()).getState(), 0);
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
        BiomeProvider biomeProvider = pack.getBiomeProvider();
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
        BiomeProvider biomeProvider = pack.getBiomeProvider();
        for(int y = properties.getMaxHeight(); y >= properties.getMinHeight(); y--) {
            array[y - properties.getMinHeight()] = ((CraftBlockData) delegate.getBlock(properties, x, y, z, biomeProvider)
                .getHandle()).getState();
        }
        return new NoiseColumn(getMinY(), array);
    }

    @Override
    public void addDebugScreenInfo(@NotNull List<String> text, @NotNull RandomState noiseConfig, @NotNull BlockPos pos) {

    }
}
