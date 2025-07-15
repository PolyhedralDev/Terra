package com.dfsek.terra.addons.image.config.noisesampler;

import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;


import com.dfsek.terra.addons.image.colorsampler.image.transform.Alignment;
import com.dfsek.terra.addons.image.image.Image;
import com.dfsek.terra.addons.image.noisesampler.CellularImageSampler;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


public class CellularImageSamplerTemplate implements ObjectTemplate<NoiseSampler> {

    @Value("image")
    private Image image;

    @Value("distance")
    @Default
    private CellularImageSampler.@Meta DistanceFunction cellularDistanceFunction = CellularImageSampler.DistanceFunction.EuclideanSq;

    @Value("return")
    @Default
    private CellularImageSampler.@Meta ReturnType cellularReturnType = CellularImageSampler.ReturnType.Distance;

    @Value("lookup")
    @Default
    private @Meta NoiseSampler lookup;

    @Value("align")
    @Default
    private @Meta Alignment align;

    @Override
    public NoiseSampler get() {
        CellularImageSampler sampler = new CellularImageSampler();
        sampler.setImage(image);
        sampler.setReturnType(cellularReturnType);
        sampler.setDistanceFunction(cellularDistanceFunction);
        sampler.setNoiseLookup(lookup);
        sampler.setAlignment(align);
        return sampler;
    }
}

