package com.dfsek.terra.config.loaders.config.sampler.templates.noise;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.config.meta.MetaValue;
import com.dfsek.terra.api.math.noise.NoiseSampler;
import com.dfsek.terra.api.math.noise.samplers.noise.CellularSampler;
import com.dfsek.terra.api.math.noise.samplers.noise.simplex.OpenSimplex2Sampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;

@SuppressWarnings("FieldMayBeFinal")
public class CellularNoiseTemplate extends NoiseTemplate<CellularSampler> {
    @Value("distance")
    @Default
    private MetaValue<CellularSampler.DistanceFunction> cellularDistanceFunction = MetaValue.of(CellularSampler.DistanceFunction.EuclideanSq);

    @Value("return")
    @Default
    private MetaValue<CellularSampler.ReturnType> cellularReturnType = MetaValue.of(CellularSampler.ReturnType.Distance);

    @Value("jitter")
    @Default
    private MetaValue<Double> cellularJitter = MetaValue.of(1.0D);


    @Value("lookup")
    @Default
    private MetaValue<NoiseSeeded> lookup = MetaValue.of(new NoiseSeeded() {
        @Override
        public NoiseSampler apply(Long seed) {

            return new OpenSimplex2Sampler((int) (long) seed);
        }

        @Override
        public int getDimensions() {
            return 2;
        }
    });

    @Override
    public NoiseSampler apply(Long seed) {
        CellularSampler sampler = new CellularSampler((int) (long) seed + salt.get());
        sampler.setNoiseLookup(lookup.get().apply(seed));
        sampler.setFrequency(frequency.get());
        sampler.setJitterModifier(cellularJitter.get());
        sampler.setReturnType(cellularReturnType.get());
        sampler.setDistanceFunction(cellularDistanceFunction.get());
        return sampler;
    }
}
