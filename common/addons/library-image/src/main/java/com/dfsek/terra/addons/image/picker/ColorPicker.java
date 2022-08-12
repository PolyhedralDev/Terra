package com.dfsek.terra.addons.image.picker;

import java.awt.image.BufferedImage;

@FunctionalInterface
public interface ColorPicker {
    
    /**
     * @param image Lookup image
     * @param x World x coordinate
     * @param z World z coordinate
     * @return Integer representing a web color
     */
    Integer apply(BufferedImage image, int x, int z);
}
