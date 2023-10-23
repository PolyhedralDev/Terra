package com.dfsek.terra.addons.terrascript.parser.lang.variables.assign;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class NumAssignmentNode extends VariableAssignmentNode<Number> {
    public NumAssignmentNode(Returnable<Number> value, Position position, int index) {
        super(value, position, index);
    }
    
    @Override
    public Number apply(ImplementationArguments implementationArguments, Scope scope) {
        return applyDouble(implementationArguments, scope);
    }
    
    @Override
    public double applyDouble(ImplementationArguments implementationArguments, Scope scope) {
        double val = value.applyDouble(implementationArguments, scope);
        scope.setNum(index, val);
        return val;
    }
}
