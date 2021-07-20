package com.dfsek.terra.addons.noise.config.templates.noise;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.addons.noise.samplers.noise.CellularSampler;
import com.dfsek.terra.addons.noise.samplers.noise.simplex.OpenSimplex2Sampler;
import com.dfsek.terra.api.noise.NoiseSampler;

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
    private NoiseSampler lookup = new OpenSimplex2Sampler();

    @Override
    public NoiseSampler get() {
        CellularSampler sampler = new CellularSampler();
        sampler.setNoiseLookup(lookup);
        sampler.setFrequency(frequency);
        sampler.setJitterModifier(cellularJitter);
        sampler.setReturnType(cellularReturnType);
        sampler.setDistanceFunction(cellularDistanceFunction);
        return sampler;
    }
}
