package com.dfsek.terra.config.loaders.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.math.Range;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.samplers.noise.random.WhiteNoiseSampler;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.world.population.items.tree.TreeLayer;

@SuppressWarnings({"FieldMayBeFinal", "unused"})
public class TreeLayerTemplate implements ObjectTemplate<TreeLayer> {
    @Value("density")
    private MetaValue<Double> density;

    @Value("y")
    private MetaValue<Range> range;

    @Value("items")
    private ProbabilityCollection<Tree> items;

    @Value("distribution")
    @Default
    private MetaValue<NoiseSeeded> sampler = MetaValue.of(new NoiseSeeded() {
        @Override
        public NoiseSampler apply(Long seed) {
            return new WhiteNoiseSampler(seed.intValue());
        }

        @Override
        public int getDimensions() {
            return 2;
        }
    });

    @Override
    public TreeLayer get() {
        return new TreeLayer(density.get(), range.get(), items, sampler.get().apply(2403L));
    }
}
