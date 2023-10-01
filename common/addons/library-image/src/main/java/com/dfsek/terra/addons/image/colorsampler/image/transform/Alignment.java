package com.dfsek.terra.addons.image.colorsampler.image.transform;

import com.dfsek.terra.addons.image.image.Image;


public enum Alignment implements ImageTransformation {
    
    NONE() {
        @Override
        public int transformX(Image image, int x) {
            return x;
        }
    
        @Override
        public int transformZ(Image image, int z) {
            return z;
        }
    },
    CENTER {
        @Override
        public int transformX(Image image, int x) {
            return x + image.getWidth() / 2;
        }
    
        @Override
        public int transformZ(Image image, int z) {
            return z + image.getHeight() / 2;
        }
    };
}
