package com.dfsek.terra.addons.noise.samplers.noise.simplex;


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

    public PseudoErosion(int octaves, double gain, double lacunarity, double slopeStrength, double branchStrength, double erosionStrength, double erosionFrequency, DerivativeNoiseSampler sampler,
                         boolean slopeMask, double slopeMaskFull, double slopeMaskNone) {
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

    public static float[] erosion(float x, float y, float dirX, float dirY) {
        float gridX = (float) Math.floor(x);
        float gridY = (float) Math.floor(y);
        float localX = x - gridX;
        float localY = y - gridY;
        float noise = 0.0f;
        float dirOutX = 0.0f;
        float dirOutY = 0.0f;
        float cumAmp = 0.0f;

        for (int cellX = -2; cellX < 2; cellX++) {
            for (int cellY = -2; cellY < 2; cellY++) {
                // TODO - Make seed affect hashing
                float cellHash = hash(gridX - (float) cellX, gridY - (float) cellY);
                float cellOffsetX = hashX(cellHash) * 0.5f;
                float cellOffsetY = hashY(cellHash) * 0.5f;
                float cellOriginDeltaX = cellX - cellOffsetX + localX;
                float cellOriginDeltaY = cellY - cellOffsetY + localY;
                float cellOriginDistSq = dot(cellOriginDeltaX, cellOriginDeltaY, cellOriginDeltaX, cellOriginDeltaY);
                float amp = (float)exp(-cellOriginDistSq * 2.0); // Exponentially decrease amplitude further from cell center
                cumAmp += amp;
                float directionalStrength = dot(cellOriginDeltaX, cellOriginDeltaY, dirX, dirY) * TAU;
                noise += (float) (MathUtil.cos(directionalStrength) * amp);
                float sinAngle = (float) MathUtil.sin(directionalStrength) * amp;
                dirOutX -= sinAngle * (cellOriginDeltaX + dirX);
                dirOutY -= sinAngle * (cellOriginDeltaY + dirY);
            }
        }

        noise /= cumAmp;
        dirOutX /= cumAmp;
        dirOutY /= cumAmp;

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