package com.dfsek.terra.biome.provider;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.BiomeHolder;
import com.dfsek.terra.biome.pipeline.BiomePipeline;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.jafama.FastMath;
import org.jetbrains.annotations.NotNull;

public class StandardBiomeProvider implements BiomeProvider {
    private final LoadingCache<Vector2, BiomeHolder> holderCache;
    private final BiomePipeline pipeline;
    private int resolution = 1;
    private final NoiseSampler xSampler;
    private final NoiseSampler zSampler;
    private final int noiseAmp;

    protected StandardBiomeProvider(BiomePipeline pipeline, TerraPlugin main, NoiseSampler xSampler, NoiseSampler zSampler, int noiseAmp) {
        this.xSampler = xSampler;
        this.zSampler = zSampler;
        this.noiseAmp = noiseAmp;
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
        x = FastMath.floorToInt(FastMath.floorDiv(x, resolution) + xSampler.getNoise(x, z) * noiseAmp);
        z = FastMath.floorToInt(FastMath.floorDiv(z, resolution) + zSampler.getNoise(x, z) * noiseAmp);
        int fdX = FastMath.floorDiv(x, pipeline.getSize());
        int fdZ = FastMath.floorDiv(z, pipeline.getSize());
        return holderCache.getUnchecked(new Vector2(fdX, fdZ)).getBiome(x - fdX * pipeline.getSize(), z - fdZ * pipeline.getSize());
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
        private int resolution = 1;
        private int noiseAmp = 2;
        private NoiseSeeded builder;

        public StandardBiomeProviderBuilder(ExceptionalFunction<Long, BiomePipeline> pipelineBuilder, TerraPlugin main) {
            this.pipelineBuilder = pipelineBuilder;
            this.main = main;
        }

        public void setResolution(int resolution) {
            this.resolution = resolution;
        }

        public void setBlender(NoiseSeeded builder) {
            this.builder = builder;
        }

        public void setNoiseAmp(int noiseAmp) {
            this.noiseAmp = noiseAmp;
        }

        @Override
        public StandardBiomeProvider build(long seed) {
            try {
                StandardBiomeProvider provider = new StandardBiomeProvider(pipelineBuilder.apply(seed), main, builder.apply(seed), builder.apply((seed + 1)), noiseAmp);
                provider.setResolution(resolution);
                return provider;
            } catch(ConfigException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
