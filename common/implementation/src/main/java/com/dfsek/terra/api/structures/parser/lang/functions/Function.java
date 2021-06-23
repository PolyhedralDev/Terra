package com.dfsek.terra.api.structures.parser.lang.functions;

import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.parser.lang.variables.Variable;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.Map;

public interface Function<T> extends Returnable<T> {
    Function<?> NULL = new Function<Object>() {
        @Override
        public ReturnType returnType() {
            return null;
        }

        @Override
        public Object apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
            return null;
        }

        @Override
        public Position getPosition() {
            return null;
        }
    };
}
