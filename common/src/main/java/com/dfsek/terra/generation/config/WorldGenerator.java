package com.dfsek.terra.generation.config;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.world.biome.Generator;
import com.dfsek.terra.api.world.palette.Palette;
import com.dfsek.terra.biome.palette.PaletteHolder;
import com.dfsek.terra.math.NoiseFunction2;
import com.dfsek.terra.math.NoiseFunction3;
import com.dfsek.terra.math.RandomFunction;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import java.util.Map;

public class WorldGenerator implements Generator {
    @SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
    private final PaletteHolder palettes;
    @SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
    private final PaletteHolder slantPalettes;

    private final Expression noiseExp;
    private final Expression elevationExp;
    private final Variable xVar;
    private final Variable yVar;
    private final Variable zVar;
    private final Variable elevationXVar;
    private final Variable elevationZVar;
    private final boolean elevationInterpolation;

    public WorldGenerator(long seed, String equation, String elevateEquation, Scope vScope, Map<String, NoiseBuilder> noiseBuilders, PaletteHolder palettes, PaletteHolder slantPalettes, boolean elevationInterpolation) {
        Parser p = new Parser();
        p.registerFunction("rand", new RandomFunction());
        Parser ep = new Parser();
        ep.registerFunction("rand", new RandomFunction());

        Scope s = new Scope().withParent(vScope);
        xVar = s.create("x");
        yVar = s.create("y");
        zVar = s.create("z");
        s.create("seed").setValue(seed);

        this.palettes = palettes;
        this.slantPalettes = slantPalettes;

        this.elevationInterpolation = elevationInterpolation;

        for(Map.Entry<String, NoiseBuilder> e : noiseBuilders.entrySet()) {
            switch(e.getValue().getDimensions()) {
                case 2:
                    p.registerFunction(e.getKey(), new NoiseFunction2(seed, e.getValue()));
                    ep.registerFunction(e.getKey(), new NoiseFunction2(seed, e.getValue()));
                    break;
                case 3:
                    p.registerFunction(e.getKey(), new NoiseFunction3(seed, e.getValue()));
                    break;
            }
        }
        try {
            this.noiseExp = p.parse(equation, s).simplify();
            if(elevateEquation != null) {
                Scope es = new Scope().withParent(vScope);
                es.create("seed").setValue(seed);
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
    public synchronized double getNoise(World world, int x, int y, int z) {
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
        return palettes.getPalette(y);
    }

    public Palette<BlockData> getSlantPalette(int y) {
        return slantPalettes.getPalette(y);
    }


    public boolean interpolateElevation() {
        return elevationInterpolation;
    }

}
