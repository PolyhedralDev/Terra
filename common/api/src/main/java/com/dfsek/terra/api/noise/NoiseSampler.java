package com.dfsek.terra.api.noise;


import com.dfsek.terra.api.vector.Vector2;
import com.dfsek.terra.api.vector.Vector3;

public interface NoiseSampler {
    static NoiseSampler zero() {
        return new NoiseSampler() {
            @Override
            public double getNoiseSeeded(long seed, double x, double y) {
                return 0;
            }

            @Override
            public double getNoiseSeeded(long seed, double x, double y, double z) {
                return 0;
            }
        };
    }

    default double getNoiseSeeded(Vector3 vector3, long seed) {
        return getNoiseSeeded(seed, vector3.getX(), vector3.getY(), vector3.getZ());
    }

    default double getNoiseSeeded(Vector2 vector2, long seed) {
        return getNoiseSeeded(seed, vector2.getX(), vector2.getZ());
    }

    double getNoiseSeeded(long seed, double x, double y);

    double getNoiseSeeded(long seed, double x, double y, double z);
}
