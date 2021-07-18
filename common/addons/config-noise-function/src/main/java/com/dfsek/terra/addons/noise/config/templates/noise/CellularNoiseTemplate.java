package com.dfsek.terra.addons.noise.config.templates.noise;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.noise.samplers.noise.CellularSampler;
import com.dfsek.terra.addons.noise.samplers.noise.simplex.OpenSimplex2Sampler;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.seeded.SeededNoiseSampler;

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
    private SeededNoiseSampler lookup = new SeededNoiseSampler() {
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
