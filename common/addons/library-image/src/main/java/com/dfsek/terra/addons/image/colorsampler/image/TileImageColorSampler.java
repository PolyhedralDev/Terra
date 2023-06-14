package com.dfsek.terra.addons.image.colorsampler.image;

import net.jafama.FastMath;

import com.dfsek.terra.addons.image.image.Image;
import com.dfsek.terra.addons.image.colorsampler.ColorSampler;
import com.dfsek.terra.addons.image.colorsampler.image.transform.ImageTransformation;


public class TileImageColorSampler implements ColorSampler {
    
    private final Image image;
    
    private final ImageTransformation transformation;
    
    public TileImageColorSampler(Image image, ImageTransformation transformation) {
        this.image = image;
        this.transformation = transformation;
    }
    
    @Override
    public int apply(int x, int z) {
        x = transformation.transformX(image, x);
        z = transformation.transformZ(image, z);
        return image.getRGB(FastMath.floorMod(x, image.getWidth()), FastMath.floorMod(z, image.getHeight()));
    }
}
