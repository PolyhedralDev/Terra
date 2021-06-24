package com.dfsek.terra.config.builder;

import com.dfsek.paralithic.eval.parser.Scope;
import com.dfsek.paralithic.eval.tokenizer.ParseException;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.UserDefinedBiome;
import com.dfsek.terra.config.loaders.config.function.FunctionTemplate;
import com.dfsek.terra.config.pack.ConfigPackImpl;
import com.dfsek.terra.config.templates.BiomeTemplate;
import com.dfsek.terra.noise.samplers.ExpressionSampler;
import com.dfsek.terra.noise.samplers.noise.ConstantSampler;
import com.dfsek.terra.world.generation.WorldGenerator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserDefinedBiomeBuilder implements BiomeBuilder {
    private final BiomeTemplate template;
    private final ConfigPackImpl pack;

    private final Map<Long, UserDefinedBiome> biomeMap = new ConcurrentHashMap<>();

    public UserDefinedBiomeBuilder(BiomeTemplate template, ConfigPackImpl pack) {
        this.template = template;
        this.pack = pack;
    }

    @Override
    public UserDefinedBiome apply(Long seed) {
        synchronized(biomeMap) {
            return biomeMap.computeIfAbsent(seed,
                    s -> {
                        NoiseSampler noise;
                        NoiseSampler elevation;
                        NoiseSampler carving;
                        Scope varScope = new Scope().withParent(pack.getVarScope());

                        template.getVariables().forEach(varScope::create);

                        Map<String, NoiseSeeded> noiseBuilderMap = pack.getTemplate().getNoiseBuilderMap();
                        Map<String, FunctionTemplate> functionTemplateMap = new LinkedHashMap<>(pack.getTemplate().getFunctions());

                        functionTemplateMap.putAll(template.getFunctions());

                        try {
                            noise = new ExpressionSampler(template.getNoiseEquation(), varScope, seed, noiseBuilderMap, functionTemplateMap);
                            elevation = template.getElevationEquation() == null ? new ConstantSampler(0) : new ExpressionSampler(template.getElevationEquation(), varScope, seed, noiseBuilderMap, functionTemplateMap);
                            carving = new ExpressionSampler(template.getCarvingEquation(), varScope, seed, noiseBuilderMap, functionTemplateMap);
                        } catch(ParseException e) {
                            throw new RuntimeException(e);
                        }

                        WorldGenerator generator = new WorldGenerator(template.getPalette(), template.getSlant(), noise, elevation, carving, template.getBiomeNoise().apply(seed), template.getElevationWeight(),
                                template.getBlendDistance(), template.getBlendStep(), template.getBlendWeight());
                        return new UserDefinedBiome(template.getVanilla(), generator, template);
                    }
            );
        }
    }

    @Override
    public ProbabilityCollection<Biome> getVanillaBiomes() {
        return template.getVanilla();
    }

    @Override
    public BiomeTemplate getTemplate() {
        return template;
    }
}
