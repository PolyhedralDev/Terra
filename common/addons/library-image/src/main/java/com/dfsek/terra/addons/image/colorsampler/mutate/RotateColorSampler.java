package com.dfsek.terra.addons.image.colorsampler.mutate;

import com.dfsek.terra.addons.image.colorsampler.ColorSampler;
import com.dfsek.terra.api.util.MathUtil;

public class RotateColorSampler implements ColorSampler {
    
    private final ColorSampler sampler;
    
    private final double radians;
    
    private final RotationMethod rotationMethod;
    
    public RotateColorSampler(ColorSampler sampler, double degrees) {
        this.sampler = sampler;
        
        double normalizedDegrees = degrees % 360.0;
        if (normalizedDegrees < 0) normalizedDegrees += 360.0;
        
        if (normalizedDegrees == 0.0)
            rotationMethod = RotationMethod.DEG_0;
        else if (normalizedDegrees == 90.0)
            rotationMethod = RotationMethod.DEG_90;
        else if (normalizedDegrees == 180.0)
            rotationMethod = RotationMethod.DEG_180;
        else if (normalizedDegrees == 270.0)
            rotationMethod = RotationMethod.DEG_270;
        else
            rotationMethod = RotationMethod.RAD_ANY;
        
        this.radians = Math.toRadians(degrees);
    }
    
    @Override
    public int apply(int x, int z) {
        int rx = switch(rotationMethod) {
            case DEG_0 -> x;
            case DEG_90 -> -z;
            case DEG_180 -> -x;
            case DEG_270 -> z;
            case RAD_ANY -> (int) (x * MathUtil.cos(radians) - z * MathUtil.sin(radians));
        };
        int rz = switch(rotationMethod) {
            case DEG_0 -> z;
            case DEG_90 -> x;
            case DEG_180 -> -z;
            case DEG_270 -> -x;
            case RAD_ANY -> (int) (z * MathUtil.cos(radians) + x * MathUtil.sin(radians));
        };
        return sampler.apply(rx, rz);
    }
    
    private enum RotationMethod {
        DEG_0,
        DEG_90,
        DEG_180,
        DEG_270,
        RAD_ANY,
    }
}
