package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.math.noise.samplers.noise.random.WhiteNoiseSampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.flora.Flora;
import com.dfsek.terra.config.loaders.Types;
import com.dfsek.terra.world.population.items.flora.FloraLayer;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class FloraLayerLoader implements TypeLoader<FloraLayer> {
    @Override
    public FloraLayer load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) o;
        double density = ((Number) map.get("density")).doubleValue();
        Range range = configLoader.loadClass(Range.class, map.get("y"));
        if(range == null) throw new LoadException("Flora range unspecified");
        ProbabilityCollection<Flora> items = (ProbabilityCollection<Flora>) configLoader.loadType(Types.FLORA_PROBABILITY_COLLECTION_TYPE, map.get("items"));

        NoiseSeeded sampler;
        if(map.containsKey("distribution")) {
            try {
                sampler = configLoader.loadClass(NoiseSeeded.class, map.get("distribution"));
            } catch(ConfigException e) {
                throw new LoadException("Unable to load noise", e);
            }
            return new FloraLayer(density, range, items, sampler.apply(2403L));
        }

        return new FloraLayer(density, range, items, new WhiteNoiseSampler(2403));
    }
}
