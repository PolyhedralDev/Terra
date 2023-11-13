package com.dfsek.terra.addons.image.util;

/**
 * Utility class for manipulating 8 bit ARGB colors
 */
public class ColorUtil {

    private ColorUtil() { }

    public static int distance(int a, int b) {
        return Math.abs(getRed(a) - getRed(b)) +
               Math.abs(getGreen(a) - getGreen(b)) +
               Math.abs(getBlue(a) - getBlue(b));
    }

    /**
     * Returns the red channel value of a given ARGB color value.
     *
     * @param argb the ARGB color value to extract the red channel value from
     *
     * @return the red channel value of the given ARGB color value, in the range 0-255
     */
    public static int getRed(int argb) {
        return argb >> 16 & 255;
    }

    /**
     * Returns the green channel value of a given ARGB color value.
     *
     * @param argb the ARGB color value to extract the green channel value from
     *
     * @return the green channel value of the given ARGB color value, in the range 0-255
     */
    public static int getGreen(int argb) {
        return argb >> 8 & 255;
    }

    /**
     * Returns the blue channel value of a given ARGB color value.
     *
     * @param argb the ARGB color value to extract the blue channel value from
     *
     * @return the blue channel value of the given ARGB color value, in the range 0-255
     */
    public static int getBlue(int argb) {
        return argb & 255;
    }

    /**
     * Returns the alpha channel value of a given ARGB color value.
     *
     * @param argb the ARGB color value to extract the blue channel value from
     *
     * @return the alpha channel value of the given ARGB color value, in the range 0-255
     */
    public static int getAlpha(int argb) {
        return argb >> 24 & 255;
    }

    /**
     * Returns the grayscale value of a given ARGB color value.
     *
     * @param argb the ARGB color value to convert to grayscale
     *
     * @return the grayscale value of the given ARGB color value, in the range 0-255
     */
    public static int getGrayscale(int argb) {
        return (getRed(argb) + getGreen(argb) + getBlue(argb)) / 3;
    }

    /**
     * Returns the value of the specified channel for a given ARGB color value.
     *
     * @param argb    the ARGB color value to extract the channel value from
     * @param channel the channel to extract the value from
     *
     * @return the value of the specified channel for the given ARGB color value, in the range 0-255
     */
    public static int getChannel(int argb, Channel channel) {
        return channel.from(argb);
    }

    /**
     * Sets the red channel value of a given ARGB color value to zero.
     *
     * @param argb the ARGB color value to zero the red channel of
     *
     * @return the resulting ARGB color value with the red channel set to zero
     */
    public static int zeroRed(int argb) {
        return argb & ~0x00FF0000;
    }

    /**
     * Sets the green channel value of a given ARGB color value to zero.
     *
     * @param argb the ARGB color value to zero the green channel of
     *
     * @return the resulting ARGB color value with the green channel set to zero
     */
    public static int zeroGreen(int argb) {
        return argb & ~0x0000FF00;
    }

    /**
     * Sets the blue channel value of a given ARGB color value to zero.
     *
     * @param argb the ARGB color value to zero the blue channel of
     *
     * @return the resulting ARGB color value with the blue channel set to zero
     */
    public static int zeroBlue(int argb) {
        return argb & ~0x000000FF;
    }

    /**
     * Sets the alpha channel value of a given ARGB color value to zero.
     * This is the same as setting the color to fully transparent.
     *
     * @param argb the ARGB color value to zero the alpha channel of
     *
     * @return the resulting ARGB color value with the alpha channel set to zero
     */
    public static int zeroAlpha(int argb) {
        return argb & ~0xFF000000;
    }

    /**
     * Sets the color channels of a given ARGB color value to zero.
     * This is the same as setting the color to black, while preserving the alpha.
     *
     * @param argb the ARGB color value to zero the color channel of
     *
     * @return the resulting ARGB color value with the color channels set to zero
     */
    public static int zeroGrayscale(int argb) {
        return argb & ~0x00FFFFFF;
    }

    /**
     * Sets the specified channel value of a given ARGB color value to zero.
     *
     * @param argb    the ARGB color value to zero the specified channel of
     * @param channel the channel to zero the value of
     *
     * @return the resulting ARGB color value with the specified channel value set to zero
     */
    public static int zeroChannel(int argb, Channel channel) {
        return channel.zero(argb);
    }

    /**
     * Multiply the RGB channels of a given ARGB color value by its alpha channel value.
     *
     * @param argb the ARGB color value to premultiply the RGB channels of
     *
     * @return the resulting premultiplied ARGB color value
     */
    public static int premultiply(int argb) {
        int alpha = getAlpha(argb);
        int red = (getRed(argb) * alpha + 127) / 255;
        int green = (getGreen(argb) * alpha + 127) / 255;
        int blue = (getBlue(argb) * alpha + 127) / 255;
        return argb(alpha, red, green, blue);
    }

    /**
     * Returns an ARGB color value with the specified values for alpha, red, green, and blue channels.
     *
     * @param alpha the alpha value, between 0 and 255, to set in the ARGB color value
     * @param red   the red value, between 0 and 255, to set in the ARGB color value
     * @param green the green value, between 0 and 255, to set in the ARGB color value
     * @param blue  the blue value, between 0 and 255, to set in the ARGB color value
     *
     * @return the resulting ARGB color value with the specified values for alpha, red, green, and blue channels
     */
    public static int argb(int alpha, int red, int green, int blue) {
        return argbAlpha(alpha) | argbRed(red) | argbGreen(green) | argbBlue(blue);
    }

    /**
     * Returns an ARGB color value with the specified values for alpha, red, green, and blue channels,
     * after validating that each channel value is in the range 0-255.
     *
     * @param alpha the alpha value, between 0 and 255, to set in the ARGB color value
     * @param red   the red value, between 0 and 255, to set in the ARGB color value
     * @param green the green value, between 0 and 255, to set in the ARGB color value
     * @param blue  the blue value, between 0 and 255, to set in the ARGB color value
     *
     * @return the resulting ARGB color value with the specified values for alpha, red, green, and blue channels
     *
     * @throws IllegalArgumentException if any channel value is outside the range 0-255
     */
    public static int argbValidated(int alpha, int red, int green, int blue) throws IllegalArgumentException {
        if(alpha < 0 || alpha > 255 ||
           red < 0 || red > 255 ||
           green < 0 || green > 255 ||
           blue < 0 || blue > 255
        ) throw new IllegalArgumentException("Channel values must be in range 0-255");
        return argb(alpha, red, green, blue);
    }

    /**
     * Returns the ARGB color value with the specified alpha channel value and zero
     * for the red, green, and blue channels.
     *
     * @param alpha the alpha channel value to set in the ARGB color value
     *
     * @return the resulting ARGB color value
     */
    public static int argbAlpha(int alpha) { return alpha << 24; }

    /**
     * Returns the ARGB color value with the specified red channel value and zero
     * for the alpha, green, and blue channels.
     *
     * @param red the red channel value to set in the ARGB color value
     *
     * @return the resulting ARGB color value
     */
    public static int argbRed(int red) { return red << 16; }

    /**
     * Returns the ARGB color value with the specified red channel value and zero
     * for the alpha, red, and blue channels.
     *
     * @param green the green channel value to set in the ARGB color value
     *
     * @return the resulting ARGB color value
     */
    public static int argbGreen(int green) { return green << 8; }

    /**
     * Returns the ARGB color value with the specified blue channel value and zero
     * for the alpha, red, and green channels.
     *
     * @param blue the blue channel value to set in the ARGB color value
     *
     * @return the resulting ARGB color value
     */
    public static int argbBlue(int blue) { return blue; }

    /**
     * Returns an ARGB color value with the specified grayscale value for all four channels.
     *
     * @param value the grayscale value to set in all four channels of the ARGB color value
     *
     * @return the resulting ARGB color value with the specified grayscale value for all four channels
     */
    public static int argbGrayscale(int value) { return argb(value, value, value, value); }

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

            @Override
            public int argb(int value) {
                return argbRed(value);
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

            @Override
            public int argb(int value) {
                return argbGreen(value);
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

            @Override
            public int argb(int value) {
                return argbBlue(value);
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

            @Override
            public int argb(int value) {
                return argbAlpha(value);
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

            @Override
            public int argb(int value) {
                return argbAlpha(value);
            }
        };

        public abstract int from(int argb);

        public abstract int zero(int argb);

        public abstract int argb(int value);
    }
}
