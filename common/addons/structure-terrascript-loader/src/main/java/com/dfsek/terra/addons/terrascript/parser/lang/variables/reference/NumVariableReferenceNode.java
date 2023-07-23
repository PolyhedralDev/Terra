package com.dfsek.terra.addons.terrascript.parser.lang.variables.reference;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;


public class NumVariableReferenceNode extends VariableReferenceNode<Number> {
    public NumVariableReferenceNode(SourcePosition position, ReturnType type, int index) {
        super(position, type, index);
    }
    
    @Override
    public Number invoke(ImplementationArguments implementationArguments, Scope scope) {
        return scope.getNum(index);
    }
    
    @Override
    public double applyDouble(ImplementationArguments implementationArguments, Scope scope) {
        return scope.getNum(index);
    }
}
