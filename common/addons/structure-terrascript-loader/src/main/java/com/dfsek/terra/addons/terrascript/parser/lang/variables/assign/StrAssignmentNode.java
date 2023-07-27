package com.dfsek.terra.addons.terrascript.parser.lang.variables.assign;

import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;


public class StrAssignmentNode extends VariableAssignmentNode<String> {
    public StrAssignmentNode(Expression<String> value, SourcePosition position, int index) {
        super(value, position, index);
    }
    
    @Override
    public ReturnType returnType() {
        return ReturnType.STRING;
    }
    
    @Override
    public String evaluate(ImplementationArguments implementationArguments, Scope scope) {
        String val = value.evaluate(implementationArguments, scope);
        scope.setStr(index, val);
        return val;
    }
    
}
