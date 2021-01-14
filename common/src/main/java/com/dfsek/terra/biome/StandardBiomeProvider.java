package com.dfsek.terra.biome;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.BiomeHolder;
import com.dfsek.terra.biome.pipeline.BiomePipeline;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;

public class StandardBiomeProvider implements BiomeProvider {
    private final LoadingCache<Vector2, BiomeHolder> holderCache;
    private final LoadingCache<Vector2, TerraBiome> biomeCache;
    private int resolution = 1;

    protected StandardBiomeProvider(BiomePipeline pipeline, TerraPlugin main) {
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
        biomeCache = CacheBuilder.newBuilder()
                .maximumSize(main == null ? 512 : main.getTerraConfig().getBiomeCache())
                .build(
                        new CacheLoader<Vector2, TerraBiome>() {
                            @Override
                            public TerraBiome load(@NotNull Vector2 key) throws ExecutionException {
                                int x = FastMath.floorToInt(key.getX());
                                int z = FastMath.floorToInt(key.getZ());
                                x /= resolution;
                                z /= resolution;
                                int fdX = FastMath.floorDiv(x, pipeline.getSize());
                                int fdZ = FastMath.floorDiv(z, pipeline.getSize());
                                return holderCache.get(new Vector2(fdX, fdZ)).getBiome(x - fdX * pipeline.getSize(), z - fdZ * pipeline.getSize());
                            }
                        }
                );
    }

    @Override
    public TerraBiome getBiome(int x, int z) {
        try {
            return biomeCache.get(new Vector2(x, z));
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

    public interface ExceptionalFunction<I, O> {
        O apply(I in) throws ConfigException;
    }

    public static final class StandardBiomeProviderBuilder implements BiomeProviderBuilder {
        private final ExceptionalFunction<Long, BiomePipeline> pipelineBuilder;
        private final TerraPlugin main;

        public StandardBiomeProviderBuilder(ExceptionalFunction<Long, BiomePipeline> pipelineBuilder, TerraPlugin main) {
            this.pipelineBuilder = pipelineBuilder;
            this.main = main;
        }

        @Override
        public StandardBiomeProvider build(long seed) {
            try {
                return new StandardBiomeProvider(pipelineBuilder.apply(seed), main);
            } catch(ConfigException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
