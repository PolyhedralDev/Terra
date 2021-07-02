package com.dfsek.terra.addons.structure.structures.parser.lang;

import com.dfsek.terra.addons.structure.structures.parser.lang.variables.Variable;
import com.dfsek.terra.addons.structure.structures.tokenizer.Position;

import java.util.Map;

public interface Item<T> {
    T apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap);

    Position getPosition();
}
