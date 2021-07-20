package com.dfsek.terra.addons.biome;

import com.dfsek.terra.api.properties.Context;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.SeededTerraBiome;
import com.dfsek.terra.api.world.biome.Biome;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserDefinedSeededTerraBiome implements SeededTerraBiome {
    private final BiomeTemplate template;
    private final Context context = new Context();

    private final Map<Long, UserDefinedBiome> biomeMap = new ConcurrentHashMap<>();

    public UserDefinedSeededTerraBiome(BiomeTemplate template) {
        this.template = template;
    }

    @Override
    public UserDefinedBiome build(long seed) {
        synchronized(biomeMap) {
            return biomeMap.computeIfAbsent(seed,
                    s -> {
                        UserDefinedGenerator generator = new UserDefinedGenerator(template.getNoiseEquation(), template.getElevationEquation(), template.getCarvingEquation(), template.getBiomeNoise(), template.getElevationWeight(),
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
