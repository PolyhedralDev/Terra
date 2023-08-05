package com.dfsek.terra.addons.terrascript.legacy.parser.lang.variables.assign;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class BoolAssignmentNode extends VariableAssignmentNode<Boolean> {
    public BoolAssignmentNode(Expression<Boolean> value, SourcePosition position, int index) {
        super(value, position, index);
    }
    
    @Override
    public Type returnType() {
        return Type.BOOLEAN;
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
