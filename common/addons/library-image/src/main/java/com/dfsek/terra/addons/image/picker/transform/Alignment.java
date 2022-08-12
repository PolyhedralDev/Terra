package com.dfsek.terra.addons.image.picker.transform;

import java.awt.image.BufferedImage;


public enum Alignment implements ImageTransformation {
    
    NONE() {
        @Override
        public int transformX(BufferedImage image, int x) {
            return x;
        }
    
        @Override
        public int transformZ(BufferedImage image, int z) {
            return z;
        }
    },
    CENTER {
        @Override
        public int transformX(BufferedImage image, int x) {
            return x + image.getWidth() / 2;
        }
    
        @Override
        public int transformZ(BufferedImage image, int z) {
            return z + image.getHeight() / 2;
        }
    };
}
