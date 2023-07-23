package com.dfsek.terra.addons.terrascript.parser.lang.variables.assign;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Expression;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.SourcePosition;


public class NumAssignmentNode extends VariableAssignmentNode<Number> {
    public NumAssignmentNode(Expression<Number> value, SourcePosition position, int index) {
        super(value, position, index);
    }
    
    @Override
    public ReturnType returnType() {
        return ReturnType.NUMBER;
    }
    
    @Override
    public Number evaluate(ImplementationArguments implementationArguments, Scope scope) {
        return applyDouble(implementationArguments, scope);
    }
    
    @Override
    public double applyDouble(ImplementationArguments implementationArguments, Scope scope) {
        double val = value.applyDouble(implementationArguments, scope);
        scope.setNum(index, val);
        return val;
    }
}
