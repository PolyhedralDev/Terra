package com.dfsek.terra.addons.image.sampler;

import java.awt.image.BufferedImage;

import com.dfsek.terra.addons.image.sampler.transform.ImageTransformation;


public class SimpleColorSampler implements ColorSampler {
    
    private final BufferedImage image;
    
    private final Integer fallback;
    
    private final ImageTransformation transformation;
    
    public SimpleColorSampler(BufferedImage image, int fallbackColor, ImageTransformation transformation) {
        this.image = image;
        this.fallback = fallbackColor;
        this.transformation = transformation;
    }
    
    @Override
    public Integer apply(int x, int z) {
        x = transformation.transformX(image, x);
        z = transformation.transformZ(image, z);
        if(x < 0 || z < 0 || x >= image.getWidth() || z >= image.getHeight()) return fallback;
        return image.getRGB(x, z);
    }
}
