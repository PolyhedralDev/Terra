package com.dfsek.terra.addons.image.sampler.image;

import com.dfsek.terra.addons.image.image.Image;
import com.dfsek.terra.addons.image.sampler.ColorSampler;
import com.dfsek.terra.addons.image.sampler.image.transform.ImageTransformation;


public class SingleImageColorSampler implements ColorSampler {
    
    private final Image image;
    
    private final ColorSampler fallback;
    
    private final ImageTransformation transformation;
    
    public SingleImageColorSampler(Image image, ColorSampler fallback, ImageTransformation transformation) {
        this.image = image;
        this.fallback = fallback;
        this.transformation = transformation;
    }
    
    @Override
    public Integer apply(int x, int z) {
        x = transformation.transformX(image, x);
        z = transformation.transformZ(image, z);
        if(x < 0 || z < 0 || x >= image.getWidth() || z >= image.getHeight()) return fallback.apply(x, z);
        return image.getRGB(x, z);
    }
}
