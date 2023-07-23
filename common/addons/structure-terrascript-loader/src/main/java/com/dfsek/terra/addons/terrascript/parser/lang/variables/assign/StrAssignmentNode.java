package com.dfsek.terra.addons.terrascript.parser.lang.variables.assign;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;


public class StrAssignmentNode extends VariableAssignmentNode<String> {
    public StrAssignmentNode(Expression<String> value, SourcePosition position, int index) {
        super(value, position, index);
    }
    
    @Override
    public String invoke(ImplementationArguments implementationArguments, Scope scope) {
        String val = value.invoke(implementationArguments, scope);
        scope.setStr(index, val);
        return val;
    }
    
}
