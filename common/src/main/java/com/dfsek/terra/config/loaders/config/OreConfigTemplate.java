package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.world.population.items.ores.OreConfig;

@SuppressWarnings("unused")
public class OreConfigTemplate implements ObjectTemplate<OreConfig> {
    @Value("min")
    private MetaValue<Integer> min;

    @Value("max")
    private MetaValue<Integer> max;

    @Value("min-height")
    private MetaValue<Integer> minHeight;

    @Value("max-height")
    private MetaValue<Integer> maxHeight;

    @Override
    public OreConfig get() {
        return new OreConfig(new Range(min.get(), max.get()), new Range(minHeight.get(), maxHeight.get()));
    }
}
