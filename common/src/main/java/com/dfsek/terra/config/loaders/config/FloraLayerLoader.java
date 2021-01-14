package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.math.noise.samplers.FastNoiseLite;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.world.flora.Flora;
import com.dfsek.terra.config.loaders.Types;
import com.dfsek.terra.generation.config.NoiseBuilder;
import com.dfsek.terra.population.items.flora.FloraLayer;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class FloraLayerLoader implements TypeLoader<FloraLayer> {
    private final TerraPlugin main;

    public FloraLayerLoader(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public FloraLayer load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) o;
        double density = ((Number) map.get("density")).doubleValue();
        Range range = (Range) configLoader.loadType(Range.class, map.get("y"));
        if(range == null) throw new LoadException("Flora range unspecified");
        ProbabilityCollection<Flora> items = (ProbabilityCollection<Flora>) configLoader.loadType(Types.FLORA_PROBABILITY_COLLECTION_TYPE, map.get("items"));

        NoiseBuilder sampler;
        if(map.containsKey("distribution")) {
            try {
                sampler = new NoiseBuilderLoader().load(NoiseBuilder.class, map.get("distribution"), configLoader);
            } catch(ConfigException e) {
                throw new LoadException("Unable to load noise", e);
            }
            return new FloraLayer(density, range, items, sampler.build(2403), main);
        }
        sampler = new NoiseBuilder();
        sampler.setType(FastNoiseLite.NoiseType.WhiteNoise);
        sampler.setDimensions(3);

        return new FloraLayer(density, range, items, sampler.build(2403), main);
    }
}
