package com.dfsek.terra.addons.flora;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.addons.flora.flora.FloraLayer;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.Flora;

public class FloraLayerLoader implements ObjectTemplate<FloraLayer> {
    @Value("density")
    private double density;

    @Value("y")
    private Range y;

    @Value("items")
    private ProbabilityCollection<Flora> items;

    @Value("distribution")
    private NoiseSeeded distribution;


    @Override
    public FloraLayer get() {
        return new FloraLayer(density, y, items, distribution.apply(2403L));
    }
}
