package com.dfsek.terra.bukkit.nms;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.info.WorldProperties;
import com.dfsek.terra.bukkit.config.PreLoadCompatibilityOptions;


public class NMSChunkGeneratorDelegate extends ChunkGenerator {
    private final com.dfsek.terra.api.world.chunk.generation.ChunkGenerator delegate;

    private final ChunkGenerator vanilla;
    private final ConfigPack pack;
    private final NMSVersionBindings bindings;

    private final long seed;

    public NMSChunkGeneratorDelegate(ChunkGenerator vanilla, ConfigPack pack, NMSBiomeProvider biomeProvider, long seed,
                                     NMSVersionBindings bindings) {
        super(biomeProvider);
        this.delegate = pack.getGeneratorProvider().newInstance(pack);
        this.vanilla = vanilla;
        this.pack = pack;
        this.seed = seed;
        this.bindings = bindings;
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
                applyStructureBeard(structureAccessor, chunk);
                return c;
            });
    }

    protected void applyStructureBeard(StructureManager structureAccessor, ChunkAccess chunk) {
        LevelAccessor level = Reflection.STRUCTURE_MANAGER.getLevel(structureAccessor);
        BiomeProvider biomeProvider = pack.getBiomeProvider();
        PreLoadCompatibilityOptions compatibilityOptions = pack.getContext().get(PreLoadCompatibilityOptions.class);
        if(compatibilityOptions.isBeard()) {
            bindings.applyStructureBeard(delegate, structureAccessor, chunk,
                new com.dfsek.terra.bukkit.world.BukkitWorldProperties(level.getMinecraftWorld().getWorld()),
                biomeProvider, compatibilityOptions);
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
        while(y > properties.getMinHeight() && !heightmap.isOpaque().test(
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
        for(int y = properties.getMaxHeight() - 1; y >= properties.getMinHeight(); y--) {
            array[y - properties.getMinHeight()] = ((CraftBlockData) delegate.getBlock(properties, x, y, z, biomeProvider)
                .getHandle()).getState();
        }
        return new NoiseColumn(properties.getMinHeight(), array);
    }

    @Override
    public void addDebugScreenInfo(@NotNull List<String> text, @NotNull RandomState noiseConfig, @NotNull BlockPos pos) {

    }
}
