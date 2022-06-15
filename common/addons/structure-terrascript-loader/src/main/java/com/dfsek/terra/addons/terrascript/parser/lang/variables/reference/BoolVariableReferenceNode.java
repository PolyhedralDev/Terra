package com.dfsek.terra.addons.terrascript.parser.lang.variables.reference;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class BoolVariableReferenceNode extends VariableReferenceNode<Boolean> {
    public BoolVariableReferenceNode(Position position, ReturnType type, int index) {
        super(position, type, index);
    }
    
    @Override
    public Boolean apply(ImplementationArguments implementationArguments, Scope scope) {
        return scope.getBool(index);
    }
    
    @Override
    public boolean applyBoolean(ImplementationArguments implementationArguments, Scope scope) {
        return scope.getBool(index);
    }
}
