package com.dfsek.terra.addons.image.picker.transform;

import java.awt.image.BufferedImage;


public interface ImageTransformation {
    
    int transformX(BufferedImage image, int x);
    
    int transformZ(BufferedImage image, int z);
}
