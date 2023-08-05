package com.dfsek.terra.addons.terrascript.legacy.parser.lang.variables.reference;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class NumVariableReferenceNode extends VariableReferenceNode<Number> {
    public NumVariableReferenceNode(SourcePosition position, Type type, int index) {
        super(position, type, index);
    }
    
    @Override
    public Number evaluate(ImplementationArguments implementationArguments, Scope scope) {
        return scope.getNum(index);
    }
    
    @Override
    public double applyDouble(ImplementationArguments implementationArguments, Scope scope) {
        return scope.getNum(index);
    }
}
