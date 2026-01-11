package com.dfsek.terra.addons.image.colorsampler.image;

import com.dfsek.terra.addons.image.colorsampler.ColorSampler;
import com.dfsek.terra.addons.image.colorsampler.image.transform.ImageTransformation;
import com.dfsek.terra.addons.image.image.Image;


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
        return image.getRGB(Math.floorMod(x, image.getWidth()), Math.floorMod(z, image.getHeight()));
    }
}
