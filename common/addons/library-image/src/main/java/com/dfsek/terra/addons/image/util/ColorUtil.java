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
    
    public static int getChannel(int rgb, Channel channel) {
        return channel.from(rgb);
    }
    
    public static int zeroRed(int rgb) {
        return rgb & ~0x00FF0000;
    }
    
    public static int zeroGreen(int rgb) {
        return rgb & ~0x0000FF00;
    }
    
    public static int zeroBlue(int rgb) {
        return rgb & ~0x000000FF;
    }
    
    public static int zeroAlpha(int rgb) {
        return rgb & ~0xFF000000;
    }
    
    public static int zeroGrayscale(int rgb) {
        return rgb & ~0x00FFFFFF;
    }
    
    public static int zeroChannel(int rgb, Channel channel) {
        return channel.zero(rgb);
    }
    
    public enum Channel {
        RED {
            @Override
            public int from(int rgb) {
                return getRed(rgb);
            }
    
            @Override
            public int zero(int rgb) {
                return zeroRed(rgb);
            }
        },
        GREEN {
            @Override
            public int from(int rgb) {
                return getGreen(rgb);
            }
    
            @Override
            public int zero(int rgb) {
                return zeroGreen(rgb);
            }
        },
        BLUE {
            @Override
            public int from(int rgb) {
                return getBlue(rgb);
            }
    
            @Override
            public int zero(int rgb) {
                return zeroBlue(rgb);
            }
        },
        GRAYSCALE {
            @Override
            public int from(int rgb) {
                return getGrayscale(rgb);
            }
    
            @Override
            public int zero(int rgb) {
                return zeroGrayscale(rgb);
            }
        },
        ALPHA {
            @Override
            public int from(int rgb) {
                return getAlpha(rgb);
            }
    
            @Override
            public int zero(int rgb) {
                return zeroAlpha(rgb);
            }
        };
        
        public abstract int from(int rgb);
        
        public abstract int zero(int rgb);
    }
}
