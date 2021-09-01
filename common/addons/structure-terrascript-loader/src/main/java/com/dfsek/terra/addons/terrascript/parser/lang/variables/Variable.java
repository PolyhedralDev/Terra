package com.dfsek.terra.addons.terrascript.parser.lang.variables;

import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public interface Variable<T> {
    T getValue();
    
    void setValue(T value);
    
    Returnable.ReturnType getType();
    
    Position getPosition();
}
