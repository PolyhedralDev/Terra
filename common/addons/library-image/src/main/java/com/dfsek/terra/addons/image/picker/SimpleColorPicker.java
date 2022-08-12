package com.dfsek.terra.addons.image.picker;

import java.awt.image.BufferedImage;

import com.dfsek.terra.addons.image.picker.transform.ImageTransformation;


public class SimpleColorPicker implements ColorPicker {
    
    private final Integer fallback;
    
    private final ImageTransformation transformation;
    
    public SimpleColorPicker(int fallbackColor,  ImageTransformation transformation) {
        this.fallback = fallbackColor;
        this.transformation = transformation;
    }
    
    @Override
    public Integer apply(BufferedImage image, int x, int z) {
        x = transformation.transformX(image, x);
        z = transformation.transformZ(image, z);
        if(x < 0 || z < 0 || x >= image.getWidth() || z >= image.getHeight()) return fallback;
        return image.getRGB(x, z);
    }
}
