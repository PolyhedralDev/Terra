package com.dfsek.terra.biome;

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
    private final Variable noise2D;
    private final Variable noise3D;


    public UserDefinedGenerator(Scope s, Expression e, List<Variable> v) {
        this.noiseExp = e;
        this.vars = v;
        this.xVar = s.getVariable("x");
        this.yVar = s.getVariable("y");
        this.zVar = s.getVariable("z");
        this.noise2D = s.getVariable("w");
        this.noise3D = s.getVariable("t");
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
        noise2D.setValue(gen.getSimplexFractal(x, z));
        noise3D.setValue(0);
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
        noise2D.setValue(gen.getSimplexFractal(x, z));
        noise3D.setValue(gen.getSimplexFractal(x, y, z));
        return noiseExp.evaluate();
    }

    /**
     * Gets the BlocPalette to generate the biome with.
     *
     * @return BlocPalette - The biome's palette.
     */
    @Override
    public BlockPalette getPalette() {
        return null;
    }
}
