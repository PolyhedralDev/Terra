package com.dfsek.terra.addons.noise.samplers.noise;


import com.dfsek.terra.api.noise.DerivativeNoiseSampler;
import com.dfsek.terra.api.util.MathUtil;


public class PseudoErosionSampler extends NoiseFunction {
    public static final double TAU = 2.0 * Math.PI;
    private static final double HASH_X = 0.3183099f;
    private static final double HASH_Y = 0.3678794f;
    public final double gain;
    public final double lacunarity;
    public final double slopeStrength;
    public final double branchStrength;
    public final double erosionStrength;
    private final int octaves;
    private final double erosionFrequency;
    private final DerivativeNoiseSampler sampler;
    private final boolean slopeMask;
    private final double slopeMaskFullSq;
    private final double slopeMaskNoneSq;
    private final double jitter;
    private final double maxCellDistSq;
    private final double maxCellDistSqRecip;
    private final boolean averageErosionImpulses;

    public PseudoErosionSampler(int octaves, double gain, double lacunarity, double slopeStrength, double branchStrength,
                                double erosionStrength, double erosionFrequency, DerivativeNoiseSampler sampler,
                                boolean slopeMask, double slopeMaskFull, double slopeMaskNone, double jitterModifier,
                                boolean averageErosionImpulses) {
        this.octaves = octaves;
        this.gain = gain;
        this.lacunarity = lacunarity;
        this.slopeStrength = slopeStrength;
        this.branchStrength = branchStrength;
        this.erosionStrength = erosionStrength;
        this.erosionFrequency = erosionFrequency;
        this.sampler = sampler;
        this.slopeMask = slopeMask;
        // Square these values and maintain sign since they're compared to a
        // squared value, otherwise a sqrt would need to be used
        this.slopeMaskFullSq = slopeMaskFull * slopeMaskFull * Math.signum(slopeMaskFull);
        this.slopeMaskNoneSq = slopeMaskNone * slopeMaskNone * Math.signum((slopeMaskNone));
        this.jitter = 0.43701595 * jitterModifier;
        this.averageErosionImpulses = averageErosionImpulses;
        this.maxCellDistSq = 1 + jitter * jitter;
        this.maxCellDistSqRecip = 1 / maxCellDistSq;
    }

    public static double hashX(double seed, double n) {
        // Swapped the components here
        double nx = HASH_X * n * seed;
        return -1.0f + 2.0f * fract(nx);
    }

    public static double hashY(double seed, double n) {
        double ny = HASH_Y * n * seed;
        return -1.0f + 2.0f * fract(ny);
    }

    public static double fract(double x) {
        return (x - Math.floor(x));
    }

    public static double smoothstep(double edge0, double edge1, double x) {
        // Scale, bias and saturate x to 0..1 range
        x = clamp((x - edge0) / (edge1 - edge0), 0.0f, 1.0f);
        // Evaluate polynomial
        return x * x * (3 - 2 * x);
    }

    public static double clamp(double x, double minVal, double maxVal) {
        return Math.max(minVal, Math.min(maxVal, x));
    }

    public static double dot(double x1, double y1, double x2, double y2) {
        return x1 * x2 + y1 * y2;
    }

    public double[] erosion(int seed, double x, double y, double dirX, double dirY) {
        int gridX = (int) Math.round(x);
        int gridY = (int) Math.round(y);
        double noise = 0.0f;
        double dirOutX = 0.0f;
        double dirOutY = 0.0f;
        double cumAmp = 0.0f;

        for(int cellX = gridX - 1; cellX <= gridX + 1; cellX++) {
            for(int cellY = gridY - 1; cellY <= gridY + 1; cellY++) {
                double cellHash = hash(seed, cellX, cellY);
                double cellOffsetX = hashX(seed, cellHash) * jitter;
                double cellOffsetY = hashY(seed, cellHash) * jitter;
                double cellOriginDeltaX = (x - cellX) + cellOffsetX;
                double cellOriginDeltaY = (y - cellY) + cellOffsetY;
                double cellOriginDistSq = cellOriginDeltaX * cellOriginDeltaX + cellOriginDeltaY * cellOriginDeltaY;
                if(cellOriginDistSq > maxCellDistSq) continue; // Skip calculating cells too far away
                double ampTmp = (cellOriginDistSq * maxCellDistSqRecip) - 1;
                double amp = ampTmp * ampTmp; // Decrease cell amplitude further away
                cumAmp += amp;
                double directionalStrength = dot(cellOriginDeltaX, cellOriginDeltaY, dirX, dirY) * TAU;
                noise += MathUtil.cos(directionalStrength) * amp;
                double sinAngle = MathUtil.sin(directionalStrength) * amp;
                dirOutX -= sinAngle * (cellOriginDeltaX + dirX);
                dirOutY -= sinAngle * (cellOriginDeltaY + dirY);
            }
        }
        if(averageErosionImpulses && cumAmp != 0) {
            noise /= cumAmp;
            dirOutX /= cumAmp;
            dirOutY /= cumAmp;
        }
        return new double[]{ noise, dirOutX, dirOutY };
    }

    public double heightMap(long seed, double x, double y) {
        double[] sample = sampler.noised(seed, x, y);
        double height = sample[0];
        double heightDirX = sample[1];
        double heightDirY = sample[2];

        // Take the curl of the normal to get the gradient facing down the slope
        double baseDirX = heightDirY * slopeStrength;
        double baseDirY = -heightDirX * slopeStrength;

        double erosion = 0.0f;
        double dirX = 0.0f;
        double dirY = 0.0f;
        double amp = 1.0f;
        double cumAmp = 0.0f;
        double freq = 1.0f;

        // Stack erosion octaves
        for(int i = 0; i < octaves; i++) {
            double[] erosionResult = erosion((int) seed,
                x * freq * erosionFrequency,
                y * freq * erosionFrequency,
                baseDirX + dirY * branchStrength,
                baseDirY - dirX * branchStrength);
            erosion += erosionResult[0] * amp;
            dirX += erosionResult[1] * amp * freq;
            dirY += erosionResult[2] * amp * freq;
            cumAmp += amp;
            amp *= gain;
            freq *= lacunarity;
        }

        // Normalize erosion noise
        erosion /= cumAmp;
        // [-1, 1] -> [0, 1]
        erosion = erosion * 0.5F + 0.5F;

        // Without masking, erosion noise in areas with small gradients tend to produce mounds,
        // this reduces erosion amplitude towards smaller gradients to avoid this
        if(slopeMask) {
            double dirMagSq = dot(baseDirX, baseDirY, baseDirX, baseDirY);
            double flatness = smoothstep((double) slopeMaskNoneSq, slopeMaskFullSq, dirMagSq);
            erosion *= flatness;
        }

        return height + erosion * erosionStrength;
    }

    @Override
    public double getNoiseRaw(long seed, double x, double y) {
        return heightMap(seed, x, y);
    }

    @Override
    public double getNoiseRaw(long seed, double x, double y, double z) {
        return getNoiseRaw(seed, x, z);
    }
}