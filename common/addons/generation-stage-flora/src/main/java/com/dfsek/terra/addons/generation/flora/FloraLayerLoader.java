package com.dfsek.terra.addons.generation.flora;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.Range;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;
import com.dfsek.terra.api.world.Flora;

public class FloraLayerLoader implements ObjectTemplate<FloraLayer> {
    @Value("density")
    private @Meta double density;

    @Value("y")
    private @Meta Range y;

    @Value("items")
    private @Meta ProbabilityCollection<@Meta Flora> items;

    @Value("distribution")
    private @Meta NoiseSampler distribution;


    @Override
    public FloraLayer get() {
        return new FloraLayer(density, y, items, distribution);
    }
}
