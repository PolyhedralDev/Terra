package com.dfsek.terra.generation;

import com.dfsek.terra.math.NoiseFunction2;
import com.dfsek.terra.math.NoiseFunction3;
import com.dfsek.terra.util.DataUtil;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.biome.Generator;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.world.palette.Palette;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UserDefinedGenerator extends Generator {
    private final Expression noiseExp;
    private final Scope s = new Scope();
    private final Variable xVar = s.getVariable("x");
    private final Variable yVar = s.getVariable("y");
    private final Variable zVar = s.getVariable("z");
    @SuppressWarnings("unchecked")
    private final Palette<BlockData>[] palettes = new Palette[256];
    private final NoiseFunction2 n2 = new NoiseFunction2();
    private final NoiseFunction3 n3 = new NoiseFunction3();

    private final boolean preventSmooth;

    private static final Object noiseLock = new Object();


    public UserDefinedGenerator(String equation, List<Variable> v, TreeMap<Integer, Palette<BlockData>> pa, boolean preventSmooth) throws ParseException {
        Parser p = new Parser();
        p.registerFunction("noise2", n2);
        p.registerFunction("noise3", n3);
        for(int y = 0; y < 256; y++) {
            Palette<BlockData> d = DataUtil.BLANK_PALETTE;
            for(Map.Entry<Integer, Palette<BlockData>> e : pa.entrySet()) {
                if(e.getKey() >= y) {
                    d = e.getValue();
                    break;
                }
            }
            palettes[y] = d;
        }
        this.noiseExp = p.parse(equation, s);
        this.preventSmooth = preventSmooth;
    }
    /**
     * Gets the 2D noise at a pair of coordinates using the provided FastNoiseLite instance.
     *
     * @param gen - The FastNoiseLite instance to use.
     * @param x   - The x coordinate.
     * @param z   - The z coordinate.
     * @return double - Noise value at the specified coordinates.
     */
    @Override
    public double getNoise(FastNoiseLite gen, World w, int x, int z) {
        synchronized(noiseLock) {
            xVar.setValue(x);
            yVar.setValue(0);
            zVar.setValue(z);
            n2.setNoise(gen);
            n3.setNoise(gen);
            return noiseExp.evaluate();
        }
    }

    /**
     * Gets the 3D noise at a pair of coordinates using the provided FastNoiseLite instance.
     *
     * @param gen - The FastNoiseLite instance to use.
     * @param x   - The x coordinate.
     * @param y   - The y coordinate.
     * @param z   - The z coordinate.
     * @return double - Noise value at the specified coordinates.
     */
    @Override
    public double getNoise(FastNoiseLite gen, World w, int x, int y, int z) {
        synchronized(noiseLock) {
            xVar.setValue(x);
            yVar.setValue(y);
            zVar.setValue(z);
            n2.setNoise(gen);
            n3.setNoise(gen);
            return noiseExp.evaluate();
        }
    }

    /**
     * Gets the BlocPalette to generate the biome with.
     *
     * @return BlocPalette - The biome's palette.
     */
    @Override
    public Palette<BlockData> getPalette(int y) {
        return palettes[y];
    }

    @Override
    public boolean useMinimalInterpolation() {
        return preventSmooth;
    }
}
