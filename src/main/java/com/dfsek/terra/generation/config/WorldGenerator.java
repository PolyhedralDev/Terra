package com.dfsek.terra.generation.config;

import com.dfsek.terra.Debug;
import com.dfsek.terra.config.genconfig.noise.NoiseConfig;
import com.dfsek.terra.generation.ElevationEquation;
import com.dfsek.terra.math.NoiseFunction2;
import com.dfsek.terra.math.NoiseFunction3;
import org.bukkit.World;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import java.util.Map;

public class WorldGenerator {
    private final ElevationEquation elevationEquation;

    private final Expression noiseExp;
    private final Scope s = new Scope();
    private final Variable xVar = s.getVariable("x");
    private final Variable yVar = s.getVariable("y");
    private final Variable zVar = s.getVariable("z");

    public WorldGenerator(World w, String equation, String elevateEquation, Map<String, Double> userVariables, Map<String, NoiseConfig> noiseBuilders) {
        for(Map.Entry<String, Double> entry : userVariables.entrySet()) {
            s.getVariable(entry.getKey()).setValue(entry.getValue()); // Define all user variables.
        }
        Parser p = new Parser();

        for(Map.Entry<String, NoiseConfig> e : noiseBuilders.entrySet()) {
            switch(e.getValue().getDimensions()) {
                case 2:
                    NoiseFunction2 function2 = new NoiseFunction2(w, e.getValue().getBuilder());
                    p.registerFunction(e.getKey(), function2);
                    break;
                case 3:
                    NoiseFunction3 function3 = new NoiseFunction3(w, e.getValue().getBuilder());
                    p.registerFunction(e.getKey(), function3);
                    break;
            }
        }
        try {
            this.noiseExp = p.parse(equation, s);
            if(elevateEquation != null) {
                Debug.info("Using elevation equation");
                this.elevationEquation = new ElevationEquation(w, elevateEquation, userVariables, noiseBuilders);
            } else this.elevationEquation = null;
        } catch(ParseException e) {
            throw new IllegalArgumentException();
        }
    }

    public double getNoise(int x, int y, int z) {
        xVar.setValue(x);
        yVar.setValue(y);
        zVar.setValue(z);
        return noiseExp.evaluate();
    }

    public ElevationEquation getElevationEquation() {
        return elevationEquation;
    }
}
