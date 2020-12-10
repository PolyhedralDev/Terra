package com.dfsek.terra.api.gaea.generation;

import com.dfsek.terra.api.gaea.biome.Biome;
import com.dfsek.terra.api.gaea.math.ChunkInterpolator;
import com.dfsek.terra.api.gaea.math.FastNoiseLite;
import com.dfsek.terra.api.gaea.profiler.ProfileFuture;
import com.dfsek.terra.api.gaea.profiler.WorldProfiler;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public abstract class GaeaChunkGenerator extends ChunkGenerator {
    private final ChunkInterpolator.InterpolationType interpolationType;
    private FastNoiseLite gen;
    private WorldProfiler profiler;

    public GaeaChunkGenerator(ChunkInterpolator.InterpolationType type) {
        interpolationType = type;
    }

    @Override
    public @NotNull ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int chunkX, int chunkZ, @NotNull BiomeGrid biome) {
        try(ProfileFuture ignore = measure("TotalChunkGenTime")) {
            if(gen == null) {
                gen = new FastNoiseLite((int) world.getSeed());
                gen.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
                gen.setFractalType(FastNoiseLite.FractalType.FBm);
                gen.setFractalOctaves(getNoiseOctaves(world));
                gen.setFrequency(getNoiseFrequency(world));
            }
            ChunkData chunk;
            ChunkInterpolator interp;
            try(ProfileFuture ignored = measure("ChunkBaseGenTime")) {
                interp = interpolationType.getInstance(world, chunkX, chunkZ, this.getBiomeGrid(world), gen);
                chunk = generateBase(world, random, chunkX, chunkZ, interp);
            }
            try(ProfileFuture ignored = measure("BiomeApplyTime")) {
                com.dfsek.terra.api.gaea.biome.BiomeGrid grid = getBiomeGrid(world);
                int xOrig = (chunkX << 4);
                int zOrig = (chunkZ << 4);
                for(byte x = 0; x < 4; x++) {
                    for(byte z = 0; z < 4; z++) {
                        int cx = xOrig + (x << 2);
                        int cz = zOrig + (z << 2);
                        Biome b = grid.getBiome(cx, cz, GenerationPhase.PALETTE_APPLY);
                        biome.setBiome(x << 2, z << 2, b.getVanillaBiome());
                    }
                }
            }
            for(GenerationPopulator g : getGenerationPopulators(world)) {
                g.populate(world, chunk, random, chunkX, chunkZ, interp);
            }
            return chunk;
        }
    }

    public void attachProfiler(WorldProfiler p) {
        this.profiler = p;
    }

    public WorldProfiler getProfiler() {
        return profiler;
    }

    private ProfileFuture measure(String id) {
        if(profiler != null) return profiler.measure(id);
        return null;
    }

    public abstract ChunkData generateBase(@NotNull World world, @NotNull Random random, int x, int z, ChunkInterpolator noise);

    public abstract int getNoiseOctaves(World w);

    public abstract double getNoiseFrequency(World w);

    public abstract List<GenerationPopulator> getGenerationPopulators(World w);

    public abstract com.dfsek.terra.api.gaea.biome.BiomeGrid getBiomeGrid(World w);

    public FastNoiseLite getNoiseGenerator() {
        return gen;
    }
}
