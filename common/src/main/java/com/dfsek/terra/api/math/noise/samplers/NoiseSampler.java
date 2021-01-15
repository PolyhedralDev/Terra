package com.dfsek.terra.api.math.noise.samplers;

import com.dfsek.terra.api.math.vector.Vector2;
import com.dfsek.terra.api.math.vector.Vector3;

public interface NoiseSampler {
    /**
     * 2D noise at given position using current settings
     * <p>
     * Noise output bounded between -1...1
     */
    double getNoise(/*FNLdouble*/ double x, /*FNLdouble*/ double y);

    /**
     * 3D noise at given position using current settings
     * <p>
     * Noise output bounded between -1...1
     */
    double getNoise(/*FNLdouble*/ double x, /*FNLdouble*/ double y, /*FNLdouble*/ double z);

    default double getNoise(Vector3 vector3) {
        return getNoise(vector3.getX(), vector3.getY(), vector3.getZ());
    }

    default double getNoise(Vector2 vector2) {
        return getNoise(vector2.getX(), vector2.getZ());
    }

    double getNoiseSeeded(int seed, double x, double y);

    double getNoiseSeeded(int seed, double x, double y, double z);
}
