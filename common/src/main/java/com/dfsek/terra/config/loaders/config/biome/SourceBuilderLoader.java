package com.dfsek.terra.config.loaders.config.biome;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.util.seeded.SeededBuilder;
import com.dfsek.terra.biome.TerraBiome;
import com.dfsek.terra.biome.pipeline.source.BiomeSource;
import com.dfsek.terra.biome.pipeline.source.RandomSource;
import com.dfsek.terra.config.loaders.Types;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SourceBuilderLoader implements TypeLoader<SeededBuilder<BiomeSource>> {
    @Override
    public SeededBuilder<BiomeSource> load(Type t, Object c, ConfigLoader loader) throws LoadException {
        Map<String, Object> source = (Map<String, Object>) c;

        String type = source.get("type").toString();

        if("NOISE".equals(type)) {
            ProbabilityCollection<TerraBiome> sourceBiomes = (ProbabilityCollection<TerraBiome>) loader.loadType(Types.TERRA_BIOME_PROBABILITY_COLLECTION_TYPE, source.get("biomes"));
            NoiseSeeded sourceNoise = loader.loadClass(NoiseSeeded.class, source.get("noise"));
            return seed -> new RandomSource(sourceBiomes, sourceNoise.apply(seed));
        }
        throw new LoadException("No such loader type: " + type);
    }
}
