package com.dfsek.terra.addons.generation.tree;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.SeededNoiseSampler;
import com.dfsek.terra.api.world.Tree;

public class TreeLayerTemplate implements ObjectTemplate<TreeLayer> {
    @Value("density")
    private double density;

    @Value("y")
    private Range y;

    @Value("items")
    private ProbabilityCollection<Tree> items;

    @Value("distribution")
    private SeededNoiseSampler distribution;

    @Override
    public TreeLayer get() {
        return new TreeLayer(density, y, items, distribution.apply(2403L));
    }
}
