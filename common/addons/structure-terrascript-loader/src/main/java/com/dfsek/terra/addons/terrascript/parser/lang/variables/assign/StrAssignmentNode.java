package com.dfsek.terra.addons.terrascript.parser.lang.variables.assign;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class StrAssignmentNode extends VariableAssignmentNode<String> {
    public StrAssignmentNode(Returnable<String> value, Position position, int index) {
        super(value, position, index);
    }
    
    @Override
    public String apply(ImplementationArguments implementationArguments, Scope scope) {
        String val = value.apply(implementationArguments, scope);
        scope.setStr(index, val);
        return val;
    }
    
}
