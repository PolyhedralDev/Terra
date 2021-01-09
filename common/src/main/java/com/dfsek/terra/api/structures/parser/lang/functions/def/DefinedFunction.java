package com.dfsek.terra.api.structures.parser.lang.functions.def;

import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.functions.Function;
import com.dfsek.terra.api.structures.tokenizer.Position;

public abstract class DefinedFunction<T> implements Function<T> {
    private final FunctionBlock<T> block;
    private final String name;
    private final Position position;

    protected DefinedFunction(FunctionBlock<T> block, String name, Position position) {
        this.block = block;
        this.name = name;
        this.position = position;
    }

    @Override
    public T apply(ImplementationArguments implementationArguments) {
        return block.apply(implementationArguments);
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
