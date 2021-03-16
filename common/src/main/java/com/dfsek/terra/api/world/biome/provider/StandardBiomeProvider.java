package com.dfsek.terra.api.world.biome.provider;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.api.world.biome.pipeline.BiomeHolder;
import com.dfsek.terra.api.world.biome.pipeline.BiomePipeline;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

public class StandardBiomeProvider implements BiomeProvider {
    private final LoadingCache<Vector2, BiomeHolder> holderCache;
    private final BiomePipeline pipeline;
    private final int resolution;
    private final NoiseSampler mutator;
    private final double noiseAmp;
    private final int seed;

    public StandardBiomeProvider(BiomePipeline pipeline, TerraPlugin main, int resolution, NoiseSampler mutator, double noiseAmp, int seed) {
        this.resolution = resolution;
        this.mutator = mutator;
        this.noiseAmp = noiseAmp;
        this.seed = seed;
        holderCache = CacheBuilder.newBuilder()
                .maximumSize(main == null ? 32 : main.getTerraConfig().getProviderCache())
                .build(
                        new CacheLoader<Vector2, BiomeHolder>() {
                            @Override
                            public BiomeHolder load(@NotNull Vector2 key) {
                                return pipeline.getBiomes(key.getBlockX(), key.getBlockZ());
                            }
                        }
                );
        this.pipeline = pipeline;
    }

    @Override
    public TerraBiome getBiome(int x, int z) {
        x += mutator.getNoiseSeeded(seed, x, z) * noiseAmp;
        z += mutator.getNoiseSeeded(1 + seed, x, z) * noiseAmp;


        x = FastMath.floorToInt(FastMath.floorDiv(x, resolution));

        z = FastMath.floorToInt(FastMath.floorDiv(z, resolution));

        int fdX = FastMath.floorDiv(x, pipeline.getSize());
        int fdZ = FastMath.floorDiv(z, pipeline.getSize());
        return holderCache.getUnchecked(new Vector2(fdX, fdZ)).getBiome(x - fdX * pipeline.getSize(), z - fdZ * pipeline.getSize());
    }
}
