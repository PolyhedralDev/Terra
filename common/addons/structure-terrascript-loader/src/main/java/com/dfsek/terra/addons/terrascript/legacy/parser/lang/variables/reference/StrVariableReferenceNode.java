package com.dfsek.terra.addons.terrascript.legacy.parser.lang.variables.reference;

import com.dfsek.terra.addons.terrascript.Type;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.legacy.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.lexer.SourcePosition;


public class StrVariableReferenceNode extends VariableReferenceNode<String> {
    public StrVariableReferenceNode(SourcePosition position, Type type, int index) {
        super(position, type, index);
    }
    
    @Override
    public String evaluate(ImplementationArguments implementationArguments, Scope scope) {
        return scope.getStr(index);
    }
}
