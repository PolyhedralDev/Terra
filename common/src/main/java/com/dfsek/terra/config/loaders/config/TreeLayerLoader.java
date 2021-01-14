package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.ProbabilityCollection;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.math.noise.samplers.FastNoiseLite;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.world.tree.Tree;
import com.dfsek.terra.config.loaders.Types;
import com.dfsek.terra.generation.config.NoiseBuilder;
import com.dfsek.terra.population.items.tree.TreeLayer;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class TreeLayerLoader implements TypeLoader<TreeLayer> {
    private final TerraPlugin main;

    public TreeLayerLoader(TerraPlugin main) {
        this.main = main;
    }

    @Override
    public TreeLayer load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) o;
        double density = ((Number) map.get("density")).doubleValue();
        Range range = (Range) configLoader.loadType(Range.class, map.get("y"));
        if(range == null) throw new LoadException("Tree range unspecified");
        ProbabilityCollection<Tree> items = (ProbabilityCollection<Tree>) configLoader.loadType(Types.TREE_PROBABILITY_COLLECTION_TYPE, map.get("items"));

        NoiseBuilder sampler = new NoiseBuilder();
        if(map.containsKey("distribution")) {
            try {
                configLoader.load(sampler, new Configuration((Map<String, Object>) map.get("distribution")));
            } catch(ConfigException e) {
                throw new LoadException("Unable to load noise", e);
            }
            return new TreeLayer(density, range, items, sampler.build(2403), main);
        }

        sampler.setType(FastNoiseLite.NoiseType.WhiteNoise);
        sampler.setDimensions(3);

        return new TreeLayer(density, range, items, sampler.build(2403), main);
    }
}
