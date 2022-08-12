package com.dfsek.terra.addons.image.picker;

import net.jafama.FastMath;

import java.awt.image.BufferedImage;

import com.dfsek.terra.addons.image.picker.transform.ImageTransformation;


public class TileColorPicker implements ColorPicker {
    
    private final ImageTransformation transformation;
    
    public TileColorPicker(ImageTransformation transformation) {
        this.transformation = transformation;
    }
    
    @Override
    public Integer apply(BufferedImage image, int x, int z) {
        x = transformation.transformX(image, x);
        z = transformation.transformZ(image, z);
        return image.getRGB(FastMath.floorMod(x, image.getWidth()), FastMath.floorMod(z, image.getHeight()));
    }
}
