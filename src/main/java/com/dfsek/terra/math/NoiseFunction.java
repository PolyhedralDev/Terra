package com.dfsek.terra.math;

import parsii.eval.Function;

public interface NoiseFunction extends Function {
    void setNoise(long seed);
}
