package com.dfsek.terra.addons.image.util;

import net.jafama.FastMath;


public class ColorUtil {
    
    private ColorUtil() {}
    
    public static int distance(int a, int b) {
        return FastMath.abs(getRed(a) - getRed(b)) +
               FastMath.abs(getGreen(a) - getGreen(b)) +
               FastMath.abs(getBlue(a) - getBlue(b));
    }
    
    public static int getRed(int rgb) {
        return rgb >> 16 & 255;
    }
    
    public static int getGreen(int rgb) {
        return rgb >> 8 & 255;
    }
    
    public static int getBlue(int rgb) {
        return rgb >> 0 & 255;
    }
    
    public static int getAlpha(int rgb) {
        return rgb >> 24 & 255;
    }
    
    public static int getGrayscale(int rgb) {
        return (getRed(rgb) + getGreen(rgb) + getBlue(rgb)) / 3;
    }
    
    public enum Channel {
        RED {
            @Override
            public int from(int rgb) {
                return getRed(rgb);
            }
        },
        GREEN {
            @Override
            public int from(int rgb) {
                return getGreen(rgb);
            }
        },
        BLUE {
            @Override
            public int from(int rgb) {
                return getBlue(rgb);
            }
        },
        GRAYSCALE {
            @Override
            public int from(int rgb) {
                return getGrayscale(rgb);
            }
        },
        ALPHA {
            @Override
            public int from(int rgb) {
                return getAlpha(rgb);
            }
        };
        
        public abstract int from(int rgb);
    }
}
