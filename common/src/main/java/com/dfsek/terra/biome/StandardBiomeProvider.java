package com.dfsek.terra.biome;

import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.BiomeHolder;
import com.dfsek.terra.biome.pipeline.BiomePipeline;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class StandardBiomeProvider implements BiomeProvider {
    private final BiomePipeline pipeline;
    private final LoadingCache<Vector2, BiomeHolder> cache = CacheBuilder.newBuilder()
            .maximumSize(1024)
            .build(
                    new CacheLoader<Vector2, BiomeHolder>() {
                        @Override
                        public BiomeHolder load(@NotNull Vector2 key) {
                            return pipeline.getBiomes(key.getBlockX(), key.getBlockZ());
                        }
                    }
            );
    private int resolution = 4;

    protected StandardBiomeProvider(BiomePipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public TerraBiome getBiome(int x, int z) {
        x /= resolution;
        z /= resolution;
        try {
            return cache.get(new Vector2(FastMath.floorDiv(x, pipeline.getSize()), FastMath.floorDiv(z, pipeline.getSize()))).getBiome(FastMath.floorMod(x, pipeline.getSize()), FastMath.floorMod(z, pipeline.getSize()));
        } catch(ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public static final class StandardBiomeProviderBuilder implements BiomeProviderBuilder {
        private final Function<Long, BiomePipeline> pipelineBuilder;

        public StandardBiomeProviderBuilder(Function<Long, BiomePipeline> pipelineBuilder) {
            this.pipelineBuilder = pipelineBuilder;
        }

        @Override
        public StandardBiomeProvider build(long seed) {
            return new StandardBiomeProvider(pipelineBuilder.apply(seed));
        }
    }
}
