package com.dfsek.terra.addons.image.util;

public class MathUtil {
    private MathUtil() { }
    
    public static double lerp(double x, double x1, double y1, double x2, double y2) {
        return (((y1 - y2) * (x - x1)) / (x1 - x2)) + y1;
    }
}
