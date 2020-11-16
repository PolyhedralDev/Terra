package com.dfsek.terra.generation;

import com.dfsek.terra.Debug;
import com.dfsek.terra.config.genconfig.noise.NoiseConfig;
import com.dfsek.terra.math.NoiseFunction;
import com.dfsek.terra.math.NoiseFunction2;
import com.dfsek.terra.math.NoiseFunction3;
import com.dfsek.terra.util.DataUtil;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.Nullable;
import org.polydev.gaea.biome.Generator;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.math.Interpolator;
import org.polydev.gaea.world.palette.Palette;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UserDefinedGenerator extends Generator {
    private static final Object noiseLock = new Object();
    private final Expression noiseExp;
    private final Scope s = new Scope();
    private final Variable xVar = s.getVariable("x");
    private final Variable yVar = s.getVariable("y");
    private final Variable zVar = s.getVariable("z");
    @SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
    private final Palette<BlockData>[] palettes = new Palette[256];
    @SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
    private final Palette<BlockData>[] slantPalettes = new Palette[256];
    private final ElevationEquation elevationEquation;
    private final boolean preventSmooth;
    private boolean elevationInterpolation;
    private final List<NoiseFunction> noiseFunctions = new ArrayList<>();
    private boolean set = true;


    public UserDefinedGenerator(String equation, @Nullable String elevateEquation, Map<String, Double> userVariables, Map<Integer, Palette<BlockData>> paletteMap, Map<Integer, Palette<BlockData>> slantPaletteMap, Map<String, NoiseConfig> noiseBuilders, boolean preventSmooth)
            throws ParseException {
        for(Map.Entry<String, Double> entry : userVariables.entrySet()) {
            s.getVariable(entry.getKey()).setValue(entry.getValue()); // Define all user variables.
        }
        Parser p = new Parser();

        for(Map.Entry<String, NoiseConfig> e : noiseBuilders.entrySet()) {
            switch(e.getValue().getDimensions()) {
                case 2:
                    NoiseFunction2 function2 = new NoiseFunction2(e.getValue().getBuilder());
                    noiseFunctions.add(function2);
                    p.registerFunction(e.getKey(), function2);
                    break;
                case 3:
                    NoiseFunction3 function3 = new NoiseFunction3(e.getValue().getBuilder());
                    noiseFunctions.add(function3);
                    p.registerFunction(e.getKey(), function3);
                    break;
            }
        }

        for(int y = 0; y < 256; y++) {
            Palette<BlockData> d = DataUtil.BLANK_PALETTE;
            for(Map.Entry<Integer, Palette<BlockData>> e : paletteMap.entrySet()) {
                if(e.getKey() >= y) {
                    d = e.getValue();
                    break;
                }
            }
            palettes[y] = d;
            Palette<BlockData> slantPalette = null;
            for(Map.Entry<Integer, Palette<BlockData>> e : slantPaletteMap.entrySet()) {
                if(e.getKey() >= y) {
                    slantPalette = e.getValue();
                    break;
                }
            }
            slantPalettes[y] = slantPalette;
        }
        if(elevateEquation != null) {
            Debug.info("Using elevation equation");
            this.elevationEquation = new ElevationEquation(elevateEquation, noiseBuilders);
        } else this.elevationEquation = null;
        this.noiseExp = p.parse(equation, s);
        this.preventSmooth = preventSmooth;
    }

    private void setNoise(long seed) {
        if(set) {
            set = false;
            for(NoiseFunction n : noiseFunctions) {
                n.setNoise(seed);
            }
        }
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
            setNoise(w.getSeed());
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
            setNoise(w.getSeed());
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

    public Palette<BlockData> getSlantPalette(int y) {
        return slantPalettes[y];
    }


    @Override
    public boolean useMinimalInterpolation() {
        return preventSmooth;
    }

    @Override
    public Interpolator.Type getInterpolationType() {
        return Interpolator.Type.LINEAR;
    }

    public ElevationEquation getElevationEquation() {
        return elevationEquation;
    }

    public boolean interpolateElevation() {
        return elevationInterpolation;
    }

    public void setElevationInterpolation(boolean elevationInterpolation) {
        this.elevationInterpolation = elevationInterpolation;
    }
}
