package com.dfsek.terra.addons.image.util;

import net.jafama.FastMath;


public class ColorUtil {
    
    private ColorUtil() {}
    
    public static int distance(int a, int b) {
        return FastMath.abs(getRed(a) - getRed(b)) +
               FastMath.abs(getGreen(a) - getGreen(b)) +
               FastMath.abs(getBlue(a) - getBlue(b));
    }
    
    public static int getRed(int argb) {
        return argb >> 16 & 255;
    }
    
    public static int getGreen(int argb) {
        return argb >> 8 & 255;
    }
    
    public static int getBlue(int argb) {
        return argb >> 0 & 255;
    }
    
    public static int getAlpha(int argb) {
        return argb >> 24 & 255;
    }
    
    public static int getGrayscale(int argb) {
        return (getRed(argb) + getGreen(argb) + getBlue(argb)) / 3;
    }
    
    public static int getChannel(int argb, Channel channel) {
        return channel.from(argb);
    }
    
    public static int zeroRed(int argb) {
        return argb & ~0x00FF0000;
    }
    
    public static int zeroGreen(int argb) {
        return argb & ~0x0000FF00;
    }
    
    public static int zeroBlue(int argb) {
        return argb & ~0x000000FF;
    }
    
    public static int zeroAlpha(int argb) {
        return argb & ~0xFF000000;
    }
    
    public static int zeroGrayscale(int argb) {
        return argb & ~0x00FFFFFF;
    }
    
    public static int zeroChannel(int argb, Channel channel) {
        return channel.zero(argb);
    }
    
    /*
     * Multiply each color channel by the alpha channel
     */
    public static int premultiply(int argb) {
        int alpha = getAlpha(argb);
        int red = (getRed(argb) * alpha + 127) / 255;
        int green = (getGreen(argb) * alpha + 127) / 255;
        int blue = (getBlue(argb) * alpha + 127) / 255;
        return argb(alpha, red, green, blue);
    }
    
    public static int argb(int alpha, int red, int green, int blue) {
        return argbAlpha(alpha) | argbRed(red) | argbGreen(green) | argbBlue(blue);
    }
    
    public static int argbValidated(int alpha, int red, int green, int blue) throws IllegalArgumentException {
        if (alpha < 0 || alpha > 255 ||
            red < 0 || red > 255 ||
            green < 0 || green > 255 ||
            blue < 0 || blue > 255
        ) throw new IllegalArgumentException("Channel values must be in range 0-255");
        return argb(alpha, red, green, blue);
    }
    
    public static int argbAlpha(int alpha) { return alpha << 24; }
    
    public static int argbRed(int red) { return red << 16; }
    
    public static int argbGreen(int green) { return green << 8; }
    
    public static int argbBlue(int blue) { return blue; }
    
    public enum Channel {
        RED {
            @Override
            public int from(int argb) {
                return getRed(argb);
            }
    
            @Override
            public int zero(int argb) {
                return zeroRed(argb);
            }
        },
        GREEN {
            @Override
            public int from(int argb) {
                return getGreen(argb);
            }
    
            @Override
            public int zero(int argb) {
                return zeroGreen(argb);
            }
        },
        BLUE {
            @Override
            public int from(int argb) {
                return getBlue(argb);
            }
    
            @Override
            public int zero(int argb) {
                return zeroBlue(argb);
            }
        },
        GRAYSCALE {
            @Override
            public int from(int argb) {
                return getGrayscale(argb);
            }
    
            @Override
            public int zero(int argb) {
                return zeroGrayscale(argb);
            }
        },
        ALPHA {
            @Override
            public int from(int argb) {
                return getAlpha(argb);
            }
    
            @Override
            public int zero(int argb) {
                return zeroAlpha(argb);
            }
        };
        
        public abstract int from(int argb);
        
        public abstract int zero(int argb);
    }
}
