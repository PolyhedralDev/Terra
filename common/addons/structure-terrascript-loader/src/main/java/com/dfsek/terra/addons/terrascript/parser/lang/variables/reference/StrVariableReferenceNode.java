package com.dfsek.terra.addons.terrascript.parser.lang.variables.reference;

import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;
import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;


public class StrVariableReferenceNode extends VariableReferenceNode<String> {
    public StrVariableReferenceNode(SourcePosition position, ReturnType type, int index) {
        super(position, type, index);
    }
    
    @Override
    public String evaluate(ImplementationArguments implementationArguments, Scope scope) {
        return scope.getStr(index);
    }
}
