package com.dfsek.terra.biome;

import com.dfsek.terra.Terra;
import com.dfsek.terra.math.NoiseFunction2;
import com.dfsek.terra.math.NoiseFunction3;
import org.polydev.gaea.biome.BiomeTerrain;
import org.polydev.gaea.math.FastNoise;
import org.polydev.gaea.math.parsii.eval.Expression;
import org.polydev.gaea.math.parsii.eval.Parser;
import org.polydev.gaea.math.parsii.eval.Scope;
import org.polydev.gaea.math.parsii.eval.Variable;
import org.polydev.gaea.math.parsii.tokenizer.ParseException;
import org.polydev.gaea.world.palette.BlockPalette;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UserDefinedGenerator extends BiomeTerrain {
    private final Expression noiseExp;
    private final Scope s = new Scope();
    private final Variable xVar = s.getVariable("x");;
    private final Variable yVar = s.getVariable("y");
    private final Variable zVar = s.getVariable("z");;
    private final TreeMap<Integer, BlockPalette> paletteMap;
    private final NoiseFunction2 n2 = new NoiseFunction2();
    private final NoiseFunction3 n3 = new NoiseFunction3();

    private static final Object noiseLock = new Object();


    public UserDefinedGenerator(String e, List<Variable> v, TreeMap<Integer, BlockPalette> pa) throws ParseException {
        Parser p = new Parser();
        p.registerFunction("noise2", n2);
        p.registerFunction("noise3", n3);
        this.paletteMap = pa;
        this.noiseExp = p.parse(e, s);
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
        synchronized(noiseLock) {
            xVar.setValue(x);
            yVar.setValue(0);
            zVar.setValue(z);
            n2.setNoise(gen, false);
            n3.setNoise(gen, false);
            return noiseExp.evaluate();
        }
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
        synchronized(noiseLock) {
            xVar.setValue(x);
            yVar.setValue(y);
            zVar.setValue(z);
            n2.setNoise(gen, false);
            n3.setNoise(gen, false);
            return noiseExp.evaluate();
        }
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

}
