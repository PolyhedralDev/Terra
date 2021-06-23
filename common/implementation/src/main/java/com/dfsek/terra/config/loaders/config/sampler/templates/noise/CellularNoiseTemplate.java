package com.dfsek.terra.config.loaders.config.sampler.templates.noise;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.noise.samplers.noise.CellularSampler;
import com.dfsek.terra.noise.samplers.noise.simplex.OpenSimplex2Sampler;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;

@SuppressWarnings("FieldMayBeFinal")
public class CellularNoiseTemplate extends NoiseTemplate<CellularSampler> {
    @Value("distance")
    @Default
    private CellularSampler.DistanceFunction cellularDistanceFunction = CellularSampler.DistanceFunction.EuclideanSq;

    @Value("return")
    @Default
    private CellularSampler.ReturnType cellularReturnType = CellularSampler.ReturnType.Distance;

    @Value("jitter")
    @Default
    private double cellularJitter = 1.0D;


    @Value("lookup")
    @Default
    private NoiseSeeded lookup = new NoiseSeeded() {
        @Override
        public NoiseSampler apply(Long seed) {

            return new OpenSimplex2Sampler((int) (long) seed);
        }

        @Override
        public int getDimensions() {
            return 2;
        }
    };

    @Override
    public NoiseSampler apply(Long seed) {
        CellularSampler sampler = new CellularSampler((int) (long) seed + salt);
        sampler.setNoiseLookup(lookup.apply(seed));
        sampler.setFrequency(frequency);
        sampler.setJitterModifier(cellularJitter);
        sampler.setReturnType(cellularReturnType);
        sampler.setDistanceFunction(cellularDistanceFunction);
        return sampler;
    }
}
