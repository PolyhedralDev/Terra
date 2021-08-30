package com.dfsek.terra.addons.terrascript.parser.lang;

import java.util.Map;

import com.dfsek.terra.addons.terrascript.parser.lang.variables.Variable;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public interface Item<T> {
    T apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap);
    
    Position getPosition();
}
