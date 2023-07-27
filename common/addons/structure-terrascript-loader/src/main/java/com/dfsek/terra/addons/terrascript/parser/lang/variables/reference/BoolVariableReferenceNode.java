package com.dfsek.terra.addons.terrascript.parser.lang.variables.reference;

import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;


public class BoolVariableReferenceNode extends VariableReferenceNode<Boolean> {
    public BoolVariableReferenceNode(SourcePosition position, ReturnType type, int index) {
        super(position, type, index);
    }
    
    @Override
    public Boolean evaluate(ImplementationArguments implementationArguments, Scope scope) {
        return scope.getBool(index);
    }
    
    @Override
    public boolean applyBoolean(ImplementationArguments implementationArguments, Scope scope) {
        return scope.getBool(index);
    }
}
