package com.dfsek.terra.addons.terrascript.legacy.parser.lang.variables.reference;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class BoolVariableReferenceNode extends VariableReferenceNode<Boolean> {
    public BoolVariableReferenceNode(SourcePosition position, Type type, int index) {
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