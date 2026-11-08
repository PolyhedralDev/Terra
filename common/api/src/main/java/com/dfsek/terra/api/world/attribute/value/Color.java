package com.dfsek.terra.api.world.attribute.value;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Channels are in [0, 1].
 */
public record Color(float r, float g, float b, float a) {
    private static final Pattern HEX6 = Pattern.compile("#[0-9a-fA-F]{6}");
    private static final Pattern HEX8 = Pattern.compile("#[0-9a-fA-F]{8}");

    public static Color rgb(int rgb) {
        return new Color(((rgb >> 16) & 0xFF) / 255f, ((rgb >> 8) & 0xFF) / 255f, (rgb & 0xFF) / 255f, 1f);
    }

    public static Color argb(int argb) {
        return new Color(((argb >> 16) & 0xFF) / 255f, ((argb >> 8) & 0xFF) / 255f, (argb & 0xFF) / 255f, ((argb >>> 24) & 0xFF) / 255f);
    }

    /**
     * Accepts {@code "#rrggbb"}, a list of 3 floats, or an integer {@code 0xRRGGBB} (alpha bits ignored).
     */
    public static Color parseRgb(Object o) {
        if(o instanceof String s) {
            if(!HEX6.matcher(s).matches()) throw new IllegalArgumentException("invalid RGB color (expected #rrggbb): \"" + s + "\"");
            return rgb(Integer.parseInt(s.substring(1), 16));
        }
        if(o instanceof List<?> l) {
            if(l.size() != 3) throw new IllegalArgumentException("color list must have 3 elements, got " + l);
            return new Color(channel(l.get(0)), channel(l.get(1)), channel(l.get(2)), 1f);
        }
        if(o instanceof Number n) return rgb(integral(n, Integer.MAX_VALUE));
        throw new IllegalArgumentException("invalid RGB color: " + o);
    }

    /**
     * Accepts {@code "#aarrggbb"}, a list of 4 floats (r, g, b, a), or an integer {@code 0xAARRGGBB}, signed or unsigned.
     */
    public static Color parseArgb(Object o) {
        if(o instanceof String s) {
            if(!HEX8.matcher(s).matches()) throw new IllegalArgumentException("invalid ARGB color (expected #aarrggbb): \"" + s + "\"");
            return argb((int) Long.parseLong(s.substring(1), 16));
        }
        if(o instanceof List<?> l) {
            if(l.size() != 4) throw new IllegalArgumentException("color list must have 4 elements, got " + l);
            return new Color(channel(l.get(0)), channel(l.get(1)), channel(l.get(2)), channel(l.get(3)));
        }
        if(o instanceof Number n) return argb(integral(n, 0xFFFFFFFFL));
        throw new IllegalArgumentException("invalid ARGB color: " + o);
    }

    private static int integral(Number n, long max) {
        BigInteger value;
        try {
            value = exact(n).toBigIntegerExact();
        } catch(ArithmeticException | NumberFormatException e) {
            throw new IllegalArgumentException("color integer must be integral: " + n);
        }
        if(value.compareTo(BigInteger.valueOf(Integer.MIN_VALUE)) < 0 || value.compareTo(BigInteger.valueOf(max)) > 0) {
            throw new IllegalArgumentException("value " + n + " is outside [" + Integer.MIN_VALUE + ", " + max + "]");
        }
        return value.intValue();
    }

    private static BigDecimal exact(Number n) {
        if(n instanceof Integer || n instanceof Long || n instanceof Short || n instanceof Byte) return BigDecimal.valueOf(n.longValue());
        if(n instanceof BigDecimal d) return d;
        if(n instanceof BigInteger i) return new BigDecimal(i);
        if(n instanceof Double || n instanceof Float) {
            double d = n.doubleValue();
            if(!Double.isFinite(d)) throw new NumberFormatException();
            return new BigDecimal(d);
        }
        return new BigDecimal(n.toString());
    }

    private static float channel(Object o) {
        if(o instanceof Number n) return n.floatValue();
        throw new IllegalArgumentException("color channel must be a number: " + o);
    }

    private static int to255(float c) {
        return Math.max(0, Math.min(255, Math.round(c * 255)));
    }

    public int toRgbInt() {
        return (to255(r) << 16) | (to255(g) << 8) | to255(b);
    }

    public int toArgbInt() {
        return (to255(a) << 24) | toRgbInt();
    }
}
