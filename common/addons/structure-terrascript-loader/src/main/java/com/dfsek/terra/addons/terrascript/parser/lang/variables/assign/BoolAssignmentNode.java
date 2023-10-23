package com.dfsek.terra.addons.terrascript.parser.lang.variables.assign;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class BoolAssignmentNode extends VariableAssignmentNode<Boolean> {
    public BoolAssignmentNode(Returnable<Boolean> value, Position position, int index) {
        super(value, position, index);
    }
    
    @Override
    public Boolean apply(ImplementationArguments implementationArguments, Scope scope) {
        return applyBoolean(implementationArguments, scope);
    }
    
    @Override
    public boolean applyBoolean(ImplementationArguments implementationArguments, Scope scope) {
        boolean val = value.applyBoolean(implementationArguments, scope);
        scope.setBool(index, val);
        return val;
    }
}
