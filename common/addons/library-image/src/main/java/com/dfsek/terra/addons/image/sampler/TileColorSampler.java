package com.dfsek.terra.addons.image.sampler;

import net.jafama.FastMath;

import com.dfsek.terra.addons.image.image.Image;
import com.dfsek.terra.addons.image.sampler.transform.ImageTransformation;


public class TileColorSampler implements ColorSampler {
    
    private final Image image;
    
    private final ImageTransformation transformation;
    
    public TileColorSampler(Image image, ImageTransformation transformation) {
        this.image = image;
        this.transformation = transformation;
    }
    
    @Override
    public Integer apply(int x, int z) {
        x = transformation.transformX(image, x);
        z = transformation.transformZ(image, z);
        return image.getRGB(FastMath.floorMod(x, image.getWidth()), FastMath.floorMod(z, image.getHeight()));
    }
}
