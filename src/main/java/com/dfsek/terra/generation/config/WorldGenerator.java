package com.dfsek.terra.generation.config;

import com.dfsek.terra.config.genconfig.noise.NoiseConfig;
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
    @SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
    private final Palette<BlockData>[] palettes;
    @SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
    private final Palette<BlockData>[] slantPalettes;

    private final boolean preventSmooth;
    private final Expression noiseExp;
    private final Expression elevationExp;
    private final Variable xVar;
    private final Variable yVar;
    private final Variable zVar;
    private final Variable elevationXVar;
    private final Variable elevationZVar;
    private boolean elevationInterpolation = true;

    @SuppressWarnings({"rawtypes", "unchecked"})
    public WorldGenerator(long seed, String equation, String elevateEquation, Scope vScope, Map<String, NoiseConfig> noiseBuilders, Palette[] palettes, Palette[] slantPalettes, boolean preventSmooth) {
        Parser p = new Parser();
        Parser ep = new Parser();

        Scope s = new Scope().withParent(vScope);
        xVar = s.create("x");
        yVar = s.create("y");
        zVar = s.create("z");

        this.preventSmooth = preventSmooth;

        this.palettes = palettes;
        this.slantPalettes = slantPalettes;

        for(Map.Entry<String, NoiseConfig> e : noiseBuilders.entrySet()) {
            switch(e.getValue().getDimensions()) {
                case 2:
                    p.registerFunction(e.getKey(), new NoiseFunction2(seed, e.getValue().getBuilder()));
                    ep.registerFunction(e.getKey(), new NoiseFunction2(seed, e.getValue().getBuilder()));
                    break;
                case 3:
                    p.registerFunction(e.getKey(), new NoiseFunction3(seed, e.getValue().getBuilder()));
                    break;
            }
        }
        try {
            this.noiseExp = p.parse(equation, s).simplify();
            if(elevateEquation != null) {
                Scope es = new Scope().withParent(vScope);
                this.elevationXVar = es.create("x");
                this.elevationZVar = es.create("z");
                this.elevationExp = ep.parse(elevateEquation, es).simplify();
            } else {
                this.elevationExp = null;
                this.elevationXVar = null;
                this.elevationZVar = null;
            }
        } catch(ParseException e) {
            throw new IllegalArgumentException();
        }
    }

    public synchronized double getElevation(int x, int z) {
        if(elevationExp == null) return 0;
        elevationXVar.setValue(x);
        elevationZVar.setValue(z);
        return elevationExp.evaluate();
    }

    @Override
    public synchronized double getNoise(FastNoiseLite fastNoiseLite, World world, int x, int z) {
        xVar.setValue(x);
        yVar.setValue(0);
        zVar.setValue(z);
        return noiseExp.evaluate();
    }

    @Override
    public synchronized double getNoise(FastNoiseLite fastNoiseLite, World world, int x, int y, int z) {
        xVar.setValue(x);
        yVar.setValue(y);
        zVar.setValue(z);
        return noiseExp.evaluate();
    }

    /**
     * Gets the BlockPalette to generate the biome with.
     *
     * @return BlockPalette - The biome's palette.
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

    public boolean interpolateElevation() {
        return elevationInterpolation;
    }

    public WorldGenerator setElevationInterpolation(boolean elevationInterpolation) {
        this.elevationInterpolation = elevationInterpolation;
        return this;
    }
}
