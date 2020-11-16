package com.dfsek.terra.generation;

import com.dfsek.terra.config.genconfig.noise.NoiseConfig;
import com.dfsek.terra.math.NoiseFunction;
import com.dfsek.terra.math.NoiseFunction2;
import com.dfsek.terra.math.NoiseFunction3;
import org.bukkit.World;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElevationEquation {
    private static final Object noiseLock = new Object();
    private final Expression delegate;
    private final Scope s = new Scope();

    private final Variable xVar = s.getVariable("x");
    private final Variable zVar = s.getVariable("z");

    private final List<NoiseFunction> noiseFunctions = new ArrayList<>();
    private boolean set = true;

    public ElevationEquation(String equation, Map<String, NoiseConfig> noiseBuilders) throws ParseException {
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
        delegate = p.parse(equation, s);
    }

    public double getNoise(double x, double z, World w) {
        synchronized(noiseLock) {
            xVar.setValue(x);
            zVar.setValue(z);
            setNoise(w.getSeed());
            return delegate.evaluate();
        }
    }

    private void setNoise(long seed) {
        if(set) {
            set = false;
            for(NoiseFunction n : noiseFunctions) {
                n.setNoise(seed);
            }
        }
    }
}
