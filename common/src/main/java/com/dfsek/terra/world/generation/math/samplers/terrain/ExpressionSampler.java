package com.dfsek.terra.world.generation.math.samplers.terrain;

import com.dfsek.paralithic.Expression;
import com.dfsek.terra.world.generation.math.interpolation.Interpolator3;

public class ExpressionSampler implements TerrainSampler {
    private final Expression expression;

    public ExpressionSampler(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Interpolator3 sample(double x, double y, double z) {
        return (i, i1, i2) -> expression.evaluate(x, y, z);
    }

    @Override
    public int getXResolution() {
        return 1;
    }
}
