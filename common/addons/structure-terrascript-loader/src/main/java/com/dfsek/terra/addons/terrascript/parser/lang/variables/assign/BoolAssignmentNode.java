package com.dfsek.terra.addons.terrascript.parser.lang.variables.assign;

import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;


public class BoolAssignmentNode extends VariableAssignmentNode<Boolean> {
    public BoolAssignmentNode(Expression<Boolean> value, SourcePosition position, int index) {
        super(value, position, index);
    }
    
    @Override
    public ReturnType returnType() {
        return ReturnType.BOOLEAN;
    }
    
    @Override
    public Boolean evaluate(ImplementationArguments implementationArguments, Scope scope) {
        return applyBoolean(implementationArguments, scope);
    }
    
    @Override
    public boolean applyBoolean(ImplementationArguments implementationArguments, Scope scope) {
        boolean val = value.applyBoolean(implementationArguments, scope);
        scope.setBool(index, val);
        return val;
    }
}
