package com.dfsek.terra.addons.biome;

import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.BiomeBuilder;
import com.dfsek.terra.api.world.biome.Biome;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserDefinedBiomeBuilder implements BiomeBuilder {
    private final BiomeTemplate template;
    private final ConfigPack pack;

    private final Map<Long, UserDefinedBiome> biomeMap = new ConcurrentHashMap<>();

    public UserDefinedBiomeBuilder(BiomeTemplate template, ConfigPack pack) {
        this.template = template;
        this.pack = pack;
    }

    @Override
    public UserDefinedBiome apply(Long seed) {
        synchronized(biomeMap) {
            return biomeMap.computeIfAbsent(seed,
                    s -> {
                        WorldGenerator generator = new WorldGenerator(template.getPalette(), template.getSlant(), template.getNoiseEquation().apply(seed), template.getElevationEquation().apply(seed), template.getCarvingEquation().apply(seed), template.getBiomeNoise().apply(seed), template.getElevationWeight(),
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
}
