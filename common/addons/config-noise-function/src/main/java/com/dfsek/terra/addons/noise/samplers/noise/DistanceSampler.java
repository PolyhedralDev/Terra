package com.dfsek.terra.addons.noise.samplers.noise;


public class DistanceSampler extends NoiseFunction {
    
    private final DistanceFunction distanceFunction;
    private final double ox, oy, oz;
    private final boolean normalize;
    private final double radius;
    
    private final double distanceAtRadius;
    
    public DistanceSampler(DistanceFunction distanceFunction, double ox, double oy, double oz, boolean normalize, double radius) {
        frequency = 1;
        this.distanceFunction = distanceFunction;
        this.ox = ox;
        this.oy = oy;
        this.oz = oz;
        this.normalize = normalize;
        this.radius = radius;
        this.distanceAtRadius = distance2d(distanceFunction, radius, 0); // distance2d and distance3d should return the same value
    }
    
    private static double distance2d(DistanceFunction distanceFunction, double x, double z) {
        return switch(distanceFunction) {
            case Euclidean -> Math.sqrt(x * x + z * z);
            case EuclideanSq -> x * x + z * z;
            case Manhattan -> Math.abs(x) + Math.abs(z);
        };
    }
    
    private static double distance3d(DistanceFunction distanceFunction, double x, double y, double z) {
        return switch(distanceFunction) {
            case Euclidean -> Math.sqrt(x * x + y * y + z * z);
            case EuclideanSq -> x * x + y * y + z * z;
            case Manhattan -> Math.abs(x) + Math.abs(y) + Math.abs(z);
        };
    }
    
    @Override
    public double getNoiseRaw(long seed, double x, double y) {
        double dx = x - ox;
        double dy = y - oz;
        if(normalize && (Math.abs(dx) > radius || Math.abs(dy) > radius)) return 1;
        double dist = distance2d(distanceFunction, dx, dy);
        if(normalize) return Math.min(((2 * dist) / distanceAtRadius) - 1, 1);
        return dist;
    }
    
    @Override
    public double getNoiseRaw(long seed, double x, double y, double z) {
        double dx = x - ox;
        double dy = y - oy;
        double dz = z - oz;
        if(normalize && (Math.abs(dx) > radius || Math.abs(dy) > radius || Math.abs(dz) > radius)) return 1;
        double dist = distance3d(distanceFunction, dx, dy, dz);
        if(normalize) return Math.min(((2 * dist) / distanceAtRadius) - 1, 1);
        return dist;
    }
    
    public enum DistanceFunction {
        Euclidean,
        EuclideanSq,
        Manhattan
    }
}
