package com.dfsek.terra.biome;

import com.dfsek.terra.math.NoiseFunction2;
import com.dfsek.terra.math.NoiseFunction3;
import org.polydev.gaea.biome.BiomeTerrain;
import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.math.parsii.eval.Expression;
import org.polydev.gaea.math.parsii.eval.Scope;
import org.polydev.gaea.math.parsii.eval.Variable;
import org.polydev.gaea.world.BlockPalette;

import java.util.List;

public class UserDefinedGenerator extends BiomeTerrain {
    private final Expression noiseExp;
    private final List<Variable> vars;
    private final Variable xVar;
    private final Variable yVar;
    private final Variable zVar;
    private final BlockPalette p;


    public UserDefinedGenerator(Scope s, Expression e, List<Variable> v, BlockPalette p) {
        this.noiseExp = e;
        this.vars = v;
        this.p = p;
        this.xVar = s.getVariable("x");
        this.yVar = s.getVariable("y");
        this.zVar = s.getVariable("z");
    }
    /**
     * Gets the 2D noise at a pair of coordinates using the provided FastNoise instance.
     *
     * @param gen - The FastNoise instance to use.
     * @param x   - The x coordinate.
     * @param z   - The z coordinate.
     * @return double - Noise value at the specified coordinates.
     */
    @Override
    public double getNoise(FastNoise gen, int x, int z) {
        xVar.setValue(x);
        yVar.setValue(0);
        zVar.setValue(z);
        NoiseFunction2.setNoise(gen);
        NoiseFunction3.setNoise(gen);
        return noiseExp.evaluate();
    }

    /**
     * Gets the 3D noise at a pair of coordinates using the provided FastNoise instance.
     *
     * @param gen - The FastNoise instance to use.
     * @param x   - The x coordinate.
     * @param y   - The y coordinate.
     * @param z   - The z coordinate.
     * @return double - Noise value at the specified coordinates.
     */
    @Override
    public double getNoise(FastNoise gen, int x, int y, int z) {
        xVar.setValue(x);
        yVar.setValue(y);
        zVar.setValue(z);
        NoiseFunction2.setNoise(gen);
        NoiseFunction3.setNoise(gen);
        return noiseExp.evaluate();
    }

    /**
     * Gets the BlocPalette to generate the biome with.
     *
     * @return BlocPalette - The biome's palette.
     */
    @Override
    public BlockPalette getPalette() {
        return p;
    }
}
