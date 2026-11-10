package com.dfsek.terra.minecraft.generation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.densityfunction.SamplerContext;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

import java.util.function.Function;
import java.util.function.Predicate;

import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;
import com.dfsek.terra.api.world.info.WorldProperties;
import com.dfsek.terra.minecraft.config.MinecraftCompatibilityOptions;


/**
 * Structure beards, base height and base column, shared by the Fabric and Paper generators. {@code vanilla} converts a Terra
 * block state to Minecraft's.
 */
public final class TerraTerrain {
    private TerraTerrain() {
    }

    public static void beard(ChunkAccess chunk, StructureManager structures, ChunkGenerator terra, BiomeProvider biomes,
                             WorldProperties world, MinecraftCompatibilityOptions options,
                             Function<com.dfsek.terra.api.block.state.BlockState, BlockState> vanilla) {
        if(!options.isBeard() || !adaptsTerrain(chunk)) return;
        Beardifier beardifier = Beardifier.forStructuresInChunk(structures, chunk.getPos());
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        beard(chunk.getPos().getMinBlockX(), chunk.getPos().getMinBlockZ(), chunk.getMinY(), chunk.getMaxY(),
            (x, y, z) -> beardifier.sampleValue(SamplerContext.EMPTY_UNCACHED, x, y, z), terra, biomes, world, options.getBeardThreshold(),
            options.getAirThreshold(), vanilla, (x, y, z, state) -> chunk.setBlockState(pos.set(x, y, z), state, 0));
    }

    static void beard(int chunkX, int chunkZ, int minY, int maxY, Density density, ChunkGenerator terra, BiomeProvider biomes,
                      WorldProperties world, double threshold, double airThreshold,
                      Function<com.dfsek.terra.api.block.state.BlockState, BlockState> vanilla, BlockSink blocks) {
        BlockState air = Blocks.AIR.defaultBlockState();
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                int worldX = chunkX + x;
                int worldZ = chunkZ + z;
                int depth = 0;
                for(int y = maxY; y >= minY; y--) {
                    double value = density.sample(worldX, y, worldZ);
                    if(value > threshold) {
                        blocks.set(x, y, z, vanilla.apply(terra.getPalette(worldX, y, worldZ, world, biomes)
                            .get(depth++, worldX, y, worldZ, world.getSeed())));
                    } else if(value < airThreshold) {
                        blocks.set(x, y, z, air);
                    } else {
                        depth = 0;
                    }
                }
            }
        }
    }

    static boolean adaptsTerrain(ChunkAccess chunk) {
        for(Structure structure : chunk.getAllReferences().keySet()) {
            if(structure.terrainAdaptation() != TerrainAdjustment.NONE) return true;
        }
        return false;
    }

    public static int baseHeight(ChunkGenerator terra, BiomeProvider biomes, WorldProperties world, LevelHeightAccessor height, int x,
                                 int z, Predicate<BlockState> opaque, Function<com.dfsek.terra.api.block.state.BlockState, BlockState> vanilla) {
        for(int y = height.getMaxY(); y >= height.getMinY(); y--) {
            if(opaque.test(vanilla.apply(terra.getBlock(world, x, y, z, biomes)))) return y + 1;
        }
        return height.getMinY();
    }

    public static NoiseColumn baseColumn(ChunkGenerator terra, BiomeProvider biomes, WorldProperties world, LevelHeightAccessor height,
                                         int x, int z, Function<com.dfsek.terra.api.block.state.BlockState, BlockState> vanilla) {
        BlockState[] column = new BlockState[height.getHeight()];
        for(int i = 0; i < column.length; i++) {
            column[i] = vanilla.apply(terra.getBlock(world, x, height.getMinY() + i, z, biomes));
        }
        return new NoiseColumn(height.getMinY(), column);
    }

    @FunctionalInterface
    interface Density {
        double sample(int x, int y, int z);
    }

    @FunctionalInterface
    interface BlockSink {
        void set(int x, int y, int z, BlockState state);
    }
}
