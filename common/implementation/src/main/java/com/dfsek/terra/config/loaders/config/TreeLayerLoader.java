package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.loading.TypeLoader;
import com.dfsek.terra.api.math.range.ConstantRange;
import com.dfsek.terra.api.noise.samplers.noise.random.WhiteNoiseSampler;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.api.util.collections.ProbabilityCollectionImpl;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.config.loaders.Types;
import com.dfsek.terra.world.population.items.tree.TreeLayer;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("unchecked")
public class TreeLayerLoader implements TypeLoader<TreeLayer> {
    @Override
    public TreeLayer load(Type type, Object o, ConfigLoader configLoader) throws LoadException {
        Map<String, Object> map = (Map<String, Object>) o;
        double density = ((Number) map.get("density")).doubleValue();
        ConstantRange range = configLoader.loadClass(ConstantRange.class, map.get("y"));
        if(range == null) throw new LoadException("Tree range unspecified");
        ProbabilityCollectionImpl<Tree> items = (ProbabilityCollectionImpl<Tree>) configLoader.loadType(Types.TREE_PROBABILITY_COLLECTION_TYPE, map.get("items"));

        if(map.containsKey("distribution")) {
            NoiseSeeded noise = configLoader.loadClass(NoiseSeeded.class, map.get("distribution"));
            return new TreeLayer(density, range, items, noise.apply(2403L));
        }

        return new TreeLayer(density, range, items, new WhiteNoiseSampler(2403));
    }
}
