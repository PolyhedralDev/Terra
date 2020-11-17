package com.dfsek.terra.generation;

import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.eval.Variable;
import parsii.tokenizer.ParseException;

public class ElevationEquation {
    private final Expression delegate;

    private final Variable xVar;
    private final Variable zVar;

    public ElevationEquation(String elevateEquation, Scope vScope, Parser p) {
        Scope s = new Scope().withParent(vScope);
        xVar = s.create("x");
        zVar = s.create("z");

        try {
            this.delegate = p.parse(elevateEquation, s);
        } catch(ParseException e) {
            throw new IllegalArgumentException();
        }
    }

    public synchronized double getNoise(double x, double z) {
        xVar.setValue(x);
        zVar.setValue(z);
        return delegate.evaluate();
    }
}
