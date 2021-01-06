package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.math.noise.FastNoiseLite;
import com.dfsek.terra.api.world.flora.Flora;
import com.dfsek.terra.config.loaders.Types;
import com.dfsek.terra.population.items.flora.FloraLayer;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class FloraLayerLoader implements TypeLoader<FloraLayer> {
    @Override
    public FloraLayer load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) o;
        double density = ((Number) map.get("density")).doubleValue();
        Range range = (Range) configLoader.loadType(Range.class, map.get("y"));
        if(range == null) throw new LoadException("Flora range unspecified");
        ProbabilityCollection<Flora> items = (ProbabilityCollection<Flora>) configLoader.loadType(Types.FLORA_PROBABILITY_COLLECTION_TYPE, map.get("items"));

        if(map.containsKey("simplex-frequency")) {
            FastNoiseLite noiseLite = new FastNoiseLite();
            noiseLite.setFrequency((Double) map.get("simplex-frequency"));
            return new FloraLayer(density, range, items, noiseLite);
        }

        return new FloraLayer(density, range, items, null);
    }
}
