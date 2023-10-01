package com.dfsek.terra.addons.biome.pipeline.image.config;

import com.dfsek.tectonic.api.config.template.annotations.Value;
import com.dfsek.tectonic.api.config.template.object.ObjectTemplate;

import com.dfsek.terra.addons.biome.pipeline.image.ImageSource;
import com.dfsek.terra.addons.biome.pipeline.v2.api.Source;
import com.dfsek.terra.addons.biome.pipeline.v2.api.biome.PipelineBiome;
import com.dfsek.terra.addons.image.colorsampler.ColorSampler;
import com.dfsek.terra.addons.image.converter.ColorConverter;


public class ImageSourceTemplate implements ObjectTemplate<Source> {
    
    @Value("color-sampler")
    private ColorSampler colorSampler;
    
    @Value("color-conversion")
    private ColorConverter<PipelineBiome> colorConverter;
    
    @Override
    public Source get() {
        return new ImageSource(colorSampler, colorConverter);
    }
}
