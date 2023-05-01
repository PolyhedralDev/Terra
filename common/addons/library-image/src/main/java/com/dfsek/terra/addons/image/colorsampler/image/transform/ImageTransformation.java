package com.dfsek.terra.addons.image.colorsampler.image.transform;

import com.dfsek.terra.addons.image.image.Image;


public interface ImageTransformation {
    
    int transformX(Image image, int x);
    
    int transformZ(Image image, int z);
}
