package com.dfsek.terra.generation;

import com.dfsek.terra.math.NoiseFunction2;
import org.polydev.gaea.math.FastNoiseLite;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

public class ElevationEquation {
    private static final Object noiseLock = new Object();
    private final Expression delegate;
    private final Scope s = new Scope();
    private final NoiseFunction2 n2 = new NoiseFunction2();

    private final Variable xVar = s.getVariable("x");
    private final Variable zVar = s.getVariable("z");

    public ElevationEquation(String equation) throws ParseException {
        Parser p = new Parser();
        p.registerFunction("noise2", n2);
        delegate = p.parse(equation, s);
    }

    public double getNoise(double x, double z, FastNoiseLite noiseLite) {
        synchronized(noiseLock) {
            xVar.setValue(x);
            zVar.setValue(z);

            n2.setNoise(noiseLite);
            return delegate.evaluate();
        }
    }
}
