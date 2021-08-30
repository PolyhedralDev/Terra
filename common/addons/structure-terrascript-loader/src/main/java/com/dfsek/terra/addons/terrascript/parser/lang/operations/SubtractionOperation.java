package com.dfsek.terra.addons.terrascript.parser.lang.operations;

import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class SubtractionOperation extends BinaryOperation<Number, Number> {
    public SubtractionOperation(Returnable<Number> left, Returnable<Number> right, Position position) {
        super(left, right, position);
    }
    
    @Override
    public Number apply(Number left, Number right) {
        return left.doubleValue() - right.doubleValue();
    }
    
    @Override
    public ReturnType returnType() {
        return ReturnType.NUMBER;
    }
}
