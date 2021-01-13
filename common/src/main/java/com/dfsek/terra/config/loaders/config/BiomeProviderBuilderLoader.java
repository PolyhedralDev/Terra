package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.noise.samplers.FastNoiseLite;
import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.world.biome.TerraBiome;
import com.dfsek.terra.biome.BiomeProvider;
import com.dfsek.terra.biome.StandardBiomeProvider;
import com.dfsek.terra.biome.pipeline.BiomePipeline;
import com.dfsek.terra.biome.pipeline.expand.FractalExpander;
import com.dfsek.terra.biome.pipeline.mutator.SmoothMutator;
import com.dfsek.terra.biome.pipeline.source.RandomSource;
import com.dfsek.terra.biome.pipeline.stages.ExpanderStage;
import com.dfsek.terra.biome.pipeline.stages.MutatorStage;
import com.dfsek.terra.config.base.ConfigPack;
import net.jafama.FastMath;

import java.lang.reflect.Type;

public class BiomeProviderBuilderLoader implements TypeLoader<BiomeProvider.BiomeProviderBuilder> {
    private final ConfigPack pack;

    public BiomeProviderBuilderLoader(ConfigPack pack) {
        this.pack = pack;
    }

    private static NoiseSampler whiteNoise(int seed) {
        FastNoiseLite noiseLite = new FastNoiseLite(seed);
        noiseLite.setNoiseType(FastNoiseLite.NoiseType.WhiteNoise);
        return noiseLite;
    }

    @Override
    public BiomeProvider.BiomeProviderBuilder load(Type t, Object c, ConfigLoader loader) {


        return new StandardBiomeProvider.StandardBiomeProviderBuilder(seed -> {
            ProbabilityCollection<TerraBiome> biomes = new ProbabilityCollection<>();

            biomes.add(pack.getBiome("PLAINS"), 1)
                    .add(pack.getBiome("SAVANNA"), 2);
            BiomePipeline pipeline = new BiomePipeline.BiomePipelineBuilder(2)
                    .addStage(new ExpanderStage(new FractalExpander(whiteNoise(FastMath.toInt(seed)))))
                    .addStage(new ExpanderStage(new FractalExpander(whiteNoise(FastMath.toInt(seed + 1)))))
                    .addStage(new ExpanderStage(new FractalExpander(whiteNoise(FastMath.toInt(seed + 2)))))
                    .addStage(new MutatorStage(new SmoothMutator(whiteNoise(FastMath.toInt(seed + 3)))))
                    .addStage(new ExpanderStage(new FractalExpander(whiteNoise(FastMath.toInt(seed + 4)))))
                    .addStage(new ExpanderStage(new FractalExpander(whiteNoise(FastMath.toInt(seed + 5)))))
                    .build(new RandomSource(biomes, whiteNoise(FastMath.toInt(seed) + 4)));
            System.out.println(pipeline.getSize());
            return pipeline;
        });
    }
}
