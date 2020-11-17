package com.dfsek.terra.generation;

import com.dfsek.terra.config.genconfig.noise.NoiseConfig;
import com.dfsek.terra.math.NoiseFunction2;
import com.dfsek.terra.math.NoiseFunction3;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import java.util.Map;

public class ElevationEquation {
    private final Expression delegate;
    private final Scope s = new Scope();

    private final Variable xVar = s.getVariable("x");
    private final Variable zVar = s.getVariable("z");

    public ElevationEquation(long seed, String elevateEquation, Map<String, Double> userVariables, Map<String, NoiseConfig> noiseBuilders) {
        for(Map.Entry<String, Double> entry : userVariables.entrySet()) {
            s.getVariable(entry.getKey()).setValue(entry.getValue()); // Define all user variables.
        }
        Parser p = new Parser();

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
            this.delegate = p.parse(elevateEquation, s);
        } catch(ParseException e) {
            throw new IllegalArgumentException();
        }
    }

    public double getNoise(double x, double z) {
        xVar.setValue(x);
        zVar.setValue(z);
        return delegate.evaluate();
    }
}
