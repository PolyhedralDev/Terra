package com.dfsek.terra.addons.biome.pipeline.image;

import com.dfsek.terra.addons.biome.pipeline.v2.api.Source;
import com.dfsek.terra.addons.biome.pipeline.v2.api.biome.PipelineBiome;
import com.dfsek.terra.addons.image.colorsampler.ColorSampler;
import com.dfsek.terra.addons.image.converter.ColorConverter;


public class ImageSource implements Source {
    
    private final ColorSampler colorSampler;
    
    private final ColorConverter<PipelineBiome> colorConverter;
    
    public ImageSource(ColorSampler colorSampler, ColorConverter<PipelineBiome> colorConverter) {
        this.colorSampler = colorSampler;
        this.colorConverter = colorConverter;
    }
    
    @Override
    public PipelineBiome get(long seed, int x, int z) {
        return colorConverter.apply(colorSampler.apply(x, z));
    }
    
    @Override
    public Iterable<PipelineBiome> getBiomes() {
        return colorConverter.getEntries();
    }
}
