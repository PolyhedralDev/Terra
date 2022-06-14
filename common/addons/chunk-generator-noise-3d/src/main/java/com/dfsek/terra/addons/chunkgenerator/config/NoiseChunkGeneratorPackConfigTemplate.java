package com.dfsek.terra.addons.chunkgenerator.config;

import com.dfsek.tectonic.api.config.template.ConfigTemplate;
import com.dfsek.tectonic.api.config.template.annotations.Default;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.noise.NoiseSampler;


public class NoiseChunkGeneratorPackConfigTemplate implements ConfigTemplate {
    @Value("blend.terrain.elevation")
    @Default
    private @Meta int elevationBlend = 4;
    
    @Value("blend.palette.resolution")
    @Default
    private @Meta int paletteRes = 1;
    
    @Value("blend.palette.sampler")
    @Default
    private @Meta NoiseSampler paletteBlendSampler = NoiseSampler.zero();
    
    @Value("blend.palette.amplitude")
    @Default
    private @Meta double paletteBlendAmplitude = 0;
    
    @Value("carving.resolution.horizontal")
    @Default
    private @Meta int horizontalRes = 4;
    
    @Value("carving.resolution.vertical")
    @Default
    private @Meta int verticalRes = 2;
    
    public int getElevationBlend() {
        return elevationBlend;
    }
    
    public int getHorizontalRes() {
        return horizontalRes;
    }
    
    public int getVerticalRes() {
        return verticalRes;
    }
    
    public double getPaletteBlendAmplitude() {
        return paletteBlendAmplitude;
    }
    
    public int getPaletteRes() {
        return paletteRes;
    }
    
    public NoiseSampler getPaletteBlendSampler() {
        return paletteBlendSampler;
    }
}
