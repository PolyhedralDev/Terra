package com.dfsek.terra.addons.terrascript.sampler;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.functions.Function;
import com.dfsek.terra.addons.terrascript.parser.lang.variables.Variable;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;

import java.util.Map;


public class SamplerFunction implements Function<Void> {
    @Override
    public Void apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        return null;
    }
    
    @Override
    public Position getPosition() {
        return null;
    }
    
    @Override
    public ReturnType returnType() {
        return null;
    }
}
