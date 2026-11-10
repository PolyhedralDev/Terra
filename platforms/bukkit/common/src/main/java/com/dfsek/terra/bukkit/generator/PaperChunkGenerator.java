package com.dfsek.terra.bukkit.generator;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.world.info.WorldProperties;
import com.dfsek.terra.bukkit.attribute.PaperVanilla;
import com.dfsek.terra.bukkit.world.block.data.BukkitBlockState;
import com.dfsek.terra.minecraft.config.MinecraftCompatibilityOptions;
import com.dfsek.terra.minecraft.generation.TerraTerrain;


public final class PaperChunkGenerator extends ChunkGenerator {
    private final ChunkGenerator paper;
    private final BukkitChunkGeneratorWrapper generator;
    private final long seed;

    public PaperChunkGenerator(ChunkGenerator paper, BukkitChunkGeneratorWrapper generator, PaperBiomeSource biomes, long seed) {
        super(biomes);
        this.paper = paper;
        this.generator = generator;
        this.seed = seed;
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> codec() {
        return MapCodec.assumeMapUnsafe(ChunkGenerator.CODEC);
    }

    @Override
    public CompletableFuture<ChunkAccess> buildTerrain(ChunkAccess chunk, Blender blender, RandomState randomState,
                                                       StructureManager structures, BiomeManager biomeManager, WorldGenRegion region,
                                                       Set<Holder<Biome>> biomes) {
        return paper.buildTerrain(chunk, blender, randomState, structures, biomeManager, region, biomes)
            .thenApply(built -> {
                ConfigPack pack = generator.getPack();
                TerraTerrain.beard(built, structures, generator.getHandle(), pack.getBiomeProvider(), properties(built),
                    pack.getContext().get(MinecraftCompatibilityOptions.class), PaperChunkGenerator::vanilla);
                return built;
            });
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types heightmap, LevelHeightAccessor height, RandomState randomState) {
        return TerraTerrain.baseHeight(generator.getHandle(), generator.getPack().getBiomeProvider(), properties(height), height, x, z,
            heightmap.isOpaque(), PaperChunkGenerator::vanilla);
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor height, RandomState randomState) {
        return TerraTerrain.baseColumn(generator.getHandle(), generator.getPack().getBiomeProvider(), properties(height), height, x, z,
            PaperChunkGenerator::vanilla);
    }

    private static BlockState vanilla(com.dfsek.terra.api.block.state.BlockState state) {
        return PaperVanilla.blockState(((BukkitBlockState) state).getHandle());
    }

    private WorldProperties properties(LevelHeightAccessor height) {
        return new VanillaWorldProperties(seed, height.getMinY(), height.getMinY() + height.getHeight());
    }

    @Override
    public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structures) {
        paper.applyBiomeDecoration(level, chunk, structures);
    }

    @Override
    public void spawnOriginalMobs(WorldGenRegion region) {
        paper.spawnOriginalMobs(region);
    }

    @Override
    public int getGenDepth() {
        return paper.getGenDepth();
    }

    @Override
    public int getSeaLevel() {
        return paper.getSeaLevel();
    }

    @Override
    public int getMinY() {
        return paper.getMinY();
    }

    @Override
    public void addDebugScreenInfo(List<String> text, RandomState randomState, BlockPos pos, SamplerContext context) {
    }

    private record VanillaWorldProperties(long getSeed, int getMinHeight, int getMaxHeight) implements WorldProperties {
        @Override
        public Object getHandle() {
            return this;
        }
    }
}
