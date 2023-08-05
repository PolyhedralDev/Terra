package com.dfsek.terra.addons.terrascript.legacy.parser.lang.variables.assign;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class StrAssignmentNode extends VariableAssignmentNode<String> {
    public StrAssignmentNode(Expression<String> value, SourcePosition position, int index) {
        super(value, position, index);
    }
    
    @Override
    public Type returnType() {
        return Type.STRING;
    }
    
    @Override
    public String evaluate(ImplementationArguments implementationArguments, Scope scope) {
        String val = value.evaluate(implementationArguments, scope);
        scope.setStr(index, val);
        return val;
    }
    
}
