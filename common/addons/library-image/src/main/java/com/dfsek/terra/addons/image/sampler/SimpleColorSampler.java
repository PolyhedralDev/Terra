package com.dfsek.terra.addons.image.sampler;

import com.dfsek.terra.addons.image.image.Image;
import com.dfsek.terra.addons.image.sampler.transform.ImageTransformation;


public class SimpleColorSampler implements ColorSampler {
    
    private final Image image;
    
    private final Integer fallback;
    
    private final ImageTransformation transformation;
    
    public SimpleColorSampler(Image image, ColorSampler fallback, ImageTransformation transformation) {
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
