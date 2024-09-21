package com.dfsek.terra.addons.noise.samplers.noise;


import com.dfsek.terra.api.noise.DerivativeNoiseSampler;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.MathUtil;


public class PseudoErosion implements NoiseSampler {
    public static final float TAU = (float) (2.0 * Math.PI);
    private static final float HASH_X = 0.3183099f;
    private static final float HASH_Y = 0.3678794f;
    private final int octaves;
    public final double gain;
    public final double lacunarity;
    public final double slopeStrength;
    public final double branchStrength;
    public final double erosionStrength;
    private final double erosionFrequency;
    private final DerivativeNoiseSampler sampler;
    private final boolean slopeMask;
    private final double slopeMaskFullSq;
    private final double slopeMaskNoneSq;
    private final double jitter;
    private final double maxCellDistSq;
    private final double maxCellDistSqRecip;
    private final boolean averageErosionImpulses;

    public PseudoErosion(int octaves, double gain, double lacunarity, double slopeStrength, double branchStrength, double erosionStrength, double erosionFrequency, DerivativeNoiseSampler sampler,
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

    public static float hash(float x, float y) {
        float xx = x * HASH_X + HASH_Y;
        float yy = y * HASH_Y + HASH_X;

        // Swapped the components here
        return 16 * (xx * yy * (xx + yy));
    }

    public static float hashX(float n) {
        // Swapped the components here
        float nx = HASH_X * n;
        return -1.0f + 2.0f * fract(nx);
    }

    public static float hashY(float n) {
        float ny = HASH_Y * n;
        return -1.0f + 2.0f * fract(ny);
    }

    public static float fract(float x) {
        return (x - (float)Math.floor(x));
    }

    public float[] erosion(float x, float y, float dirX, float dirY) {
        int gridX = Math.round(x);
        int gridY = Math.round(y);
        float noise = 0.0f;
        float dirOutX = 0.0f;
        float dirOutY = 0.0f;
        float cumAmp = 0.0f;

        for (int cellX = gridX - 1; cellX <= gridX + 1; cellX++) {
            for (int cellY = gridY - 1; cellY <= gridY + 1; cellY++) {
                // TODO - Make seed affect hashing
                float cellHash = hash(cellX, cellY);
                float cellOffsetX = (float) (hashX(cellHash) * jitter);
                float cellOffsetY = (float) (hashY(cellHash) * jitter);
                float cellOriginDeltaX = (x - cellX) + cellOffsetX;
                float cellOriginDeltaY = (y - cellY) + cellOffsetY;
                float cellOriginDistSq = cellOriginDeltaX * cellOriginDeltaX + cellOriginDeltaY * cellOriginDeltaY;
                if (cellOriginDistSq > maxCellDistSq) continue; // Skip calculating cells too far away
                float ampTmp = (float) ((cellOriginDistSq * maxCellDistSqRecip) - 1); float amp = ampTmp * ampTmp; // Decrease cell amplitude further away
                cumAmp += amp;
                float directionalStrength = dot(cellOriginDeltaX, cellOriginDeltaY, dirX, dirY) * TAU;
                noise += (float) (MathUtil.cos(directionalStrength) * amp);
                float sinAngle = (float) MathUtil.sin(directionalStrength) * amp;
                dirOutX -= sinAngle * (cellOriginDeltaX + dirX);
                dirOutY -= sinAngle * (cellOriginDeltaY + dirY);
            }
        }
        if (averageErosionImpulses && cumAmp != 0) {
            noise /= cumAmp;
            dirOutX /= cumAmp;
            dirOutY /= cumAmp;
        }
        return new float[] {noise, dirOutX, dirOutY};
    }

    public static double exp(double val) {
        final long tmp = (long) (1512775 * val + 1072632447);
        return Double.longBitsToDouble(tmp << 32);
    }

    public static float smoothstep(float edge0, float edge1, float x) {
        // Scale, bias and saturate x to 0..1 range
        x = clamp((x - edge0) / (edge1 - edge0), 0.0f, 1.0f);
        // Evaluate polynomial
        return x * x * (3 - 2 * x);
    }

    public static float clamp(float x, float minVal, float maxVal) {
        return Math.max(minVal, Math.min(maxVal, x));
    }

    public float heightMap(long seed, float x, float y) {
        double[] sample = sampler.noised(seed, x, y);
        float height = (float) sample[0];
        float heightDirX = (float) sample[1];
        float heightDirY = (float) sample[2];

        // Take the curl of the normal to get the gradient facing down the slope
        float baseDirX = heightDirY * (float) slopeStrength;
        float baseDirY = -heightDirX * (float) slopeStrength;

        float erosion = 0.0f;
        float dirX = 0.0f;
        float dirY = 0.0f;
        float amp = 1.0f;
        float cumAmp = 0.0f;
        float freq = 1.0f;

        // Stack erosion octaves
        for (int i = 0; i < octaves; i++) {
            float[] erosionResult = erosion(
                x * freq * (float) erosionFrequency,
                y * freq * (float) erosionFrequency,
                baseDirX + dirY * (float) branchStrength,
                baseDirY - dirX * (float) branchStrength);
            erosion += erosionResult[0] * amp;
            dirX += erosionResult[1] * amp * freq;
            dirY += erosionResult[2] * amp * freq;
            cumAmp += amp;
            amp *= gain;
            freq *= lacunarity;
        }

        // TODO - Test different output ranges, see how they affect visuals
        // Normalize erosion noise
        erosion /= cumAmp;
        // [-1, 1] -> [0, 1]
        erosion = erosion * 0.5F + 0.5F;

        // Without masking, erosion noise in areas with small gradients tend to produce mounds,
        // this reduces erosion amplitude towards smaller gradients to avoid this
        if (slopeMask) {
            float dirMagSq = dot(baseDirX, baseDirY, baseDirX, baseDirY);
            float flatness = smoothstep((float) slopeMaskNoneSq, (float) slopeMaskFullSq, dirMagSq);
            erosion *= flatness;
        }

        return (float) (height + erosion * erosionStrength);
    }

    public static float dot(float x1, float y1, float x2, float y2) {
        return x1 * x2 + y1 * y2;
    }

    @Override
    public double noise(long seed, double x, double y) {
        return heightMap(seed, (float) x, (float) y);
    }

    @Override
    public double noise(long seed, double x, double y, double z) {
        return noise(seed, x, z);
    }
}