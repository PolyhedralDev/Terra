package com.dfsek.terra.addons.biome;

import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.BiomeBuilder;
import com.dfsek.terra.api.world.biome.Biome;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserDefinedBiomeBuilder implements BiomeBuilder {
    private final BiomeTemplate template;
    private final Context context = new Context();

    private final Map<Long, UserDefinedBiome> biomeMap = new ConcurrentHashMap<>();

    public UserDefinedBiomeBuilder(BiomeTemplate template) {
        this.template = template;
    }

    @Override
    public UserDefinedBiome apply(Long seed) {
        synchronized(biomeMap) {
            return biomeMap.computeIfAbsent(seed,
                    s -> {
                        UserDefinedGenerator generator = new UserDefinedGenerator(template.getNoiseEquation().apply(seed), template.getElevationEquation().apply(seed), template.getCarvingEquation().apply(seed), template.getBiomeNoise().apply(seed), template.getElevationWeight(),
                                template.getBlendDistance(), template.getBlendStep(), template.getBlendWeight());
                        return new UserDefinedBiome(template.getVanilla(), generator, template, context);
                    }
            );
        }
    }

    @Override
    public ProbabilityCollection<Biome> getVanillaBiomes() {
        return template.getVanilla();
    }

    @Override
    public Context getContext() {
        return context;
    }
}
