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
import java.util.Map;
import java.util.TreeMap;

public class UserDefinedGenerator extends BiomeTerrain {
    private final Expression noiseExp;
    private final List<Variable> vars;
    private final Variable xVar;
    private final Variable yVar;
    private final Variable zVar;
    private final TreeMap<Integer, BlockPalette> paletteMap;


    public UserDefinedGenerator(Scope s, Expression e, List<Variable> v, TreeMap<Integer, BlockPalette> p) {
        this.noiseExp = e;
        this.vars = v;
        this.paletteMap = p;
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
    public BlockPalette getPalette(int y) {
        for(Map.Entry<Integer, BlockPalette> e : paletteMap.entrySet()) {
            if(e.getKey() >= y ) return e.getValue();
        }
        return null;
    }

    private static class Range {
        private final int min;
        private final int max;

        /**
         * Instantiates a Range object with a minimum value (inclusive) and a maximum value (exclusive).
         * @param min The minimum value (inclusive).
         * @param max The maximum value (exclusive).
         */
        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }

        /**
         * Tests if a value is within range.
         * @param val The value to test.
         * @return boolean - Whether the value is within range.
         */
        public boolean isInRange(int val) {
            return val >= min && val < max;
        }
    }
}
