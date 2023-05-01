package com.dfsek.terra.addons.image.colorsampler.image;

import com.dfsek.terra.addons.image.image.Image;
import com.dfsek.terra.addons.image.colorsampler.ColorSampler;
import com.dfsek.terra.addons.image.colorsampler.image.transform.ImageTransformation;


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
        var nx = transformation.transformX(image, x);
        var nz = transformation.transformZ(image, z);
        if(nx < 0 || nz < 0 || nx >= image.getWidth() || nz >= image.getHeight()) return fallback.apply(x, z);
        return image.getRGB(nx, nz);
    }
}
