package com.dfsek.terra.addons.terrascript.parser.lang.variables;

import java.util.Map;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Item;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class Assignment<T> implements Item<T> {
    private final Returnable<T> value;
    private final Position position;
    private final String identifier;
    
    public Assignment(Returnable<T> value, String identifier, Position position) {
        this.value = value;
        this.identifier = identifier;
        this.position = position;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public synchronized T apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        T val = value.apply(implementationArguments, variableMap);
        ((Variable<T>) variableMap.get(identifier)).setValue(val);
        return val;
    }
    
    @Override
    public Position getPosition() {
        return position;
    }
}
