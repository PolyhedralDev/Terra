package com.dfsek.terra.generation.config;

import com.dfsek.terra.Debug;
import com.dfsek.terra.config.genconfig.noise.NoiseConfig;
import com.dfsek.terra.generation.ElevationEquation;
import com.dfsek.terra.math.NoiseFunction2;
import com.dfsek.terra.math.NoiseFunction3;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.polydev.gaea.biome.Generator;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.math.Interpolator;
import org.polydev.gaea.world.palette.Palette;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import java.util.Map;

public class WorldGenerator extends Generator {
    private final ElevationEquation elevationEquation;

    @SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
    private final Palette<BlockData>[] palettes;
    @SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
    private final Palette<BlockData>[] slantPalettes;

    private final boolean preventSmooth;

    private boolean elevationInterpolation = true;

    private final Expression noiseExp;
    private final Scope s = new Scope();
    private final Variable xVar = s.getVariable("x");
    private final Variable yVar = s.getVariable("y");
    private final Variable zVar = s.getVariable("z");

    @SuppressWarnings({"rawtypes", "unchecked"})
    public WorldGenerator(long seed, String equation, String elevateEquation, Map<String, Double> userVariables, Map<String, NoiseConfig> noiseBuilders, Palette[] palettes, Palette[] slantPalettes, boolean preventSmooth) {
        for(Map.Entry<String, Double> entry : userVariables.entrySet()) {
            s.getVariable(entry.getKey()).setValue(entry.getValue()); // Define all user variables.
        }
        Parser p = new Parser();

        this.preventSmooth = preventSmooth;

        this.palettes = palettes;
        this.slantPalettes = slantPalettes;

        for(Map.Entry<String, NoiseConfig> e : noiseBuilders.entrySet()) {
            switch(e.getValue().getDimensions()) {
                case 2:
                    NoiseFunction2 function2 = new NoiseFunction2(seed, e.getValue().getBuilder());
                    p.registerFunction(e.getKey(), function2);
                    break;
                case 3:
                    NoiseFunction3 function3 = new NoiseFunction3(seed, e.getValue().getBuilder());
                    p.registerFunction(e.getKey(), function3);
                    break;
            }
        }
        try {
            this.noiseExp = p.parse(equation, s);
            if(elevateEquation != null) {
                Debug.info("Using elevation equation");
                this.elevationEquation = new ElevationEquation(seed, elevateEquation, userVariables, noiseBuilders);
            } else this.elevationEquation = null;
        } catch(ParseException e) {
            throw new IllegalArgumentException();
        }
    }

    public ElevationEquation getElevationEquation() {
        return elevationEquation;
    }

    @Override
    public double getNoise(FastNoiseLite fastNoiseLite, World world, int x, int z) {
        xVar.setValue(x);
        yVar.setValue(0);
        zVar.setValue(z);
        return noiseExp.evaluate();
    }

    @Override
    public double getNoise(FastNoiseLite fastNoiseLite, World world, int x, int y, int z) {
        xVar.setValue(x);
        yVar.setValue(y);
        zVar.setValue(z);
        return noiseExp.evaluate();
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

    public ElevationEquation getElevationEquation(World w) {
        return elevationEquation;
    }

    public boolean interpolateElevation() {
        return elevationInterpolation;
    }

    public WorldGenerator setElevationInterpolation(boolean elevationInterpolation) {
        this.elevationInterpolation = elevationInterpolation;
        return this;
    }
}
