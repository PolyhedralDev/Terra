package com.dfsek.terra.addons.terrascript.parser.lang.variables.reference;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class StrVariableReferenceNode extends VariableReferenceNode<String> {
    public StrVariableReferenceNode(Position position, ReturnType type, int index) {
        super(position, type, index);
    }
    
    @Override
    public String apply(ImplementationArguments implementationArguments, Scope scope) {
        return scope.getStr(index);
    }
}
