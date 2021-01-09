package com.dfsek.terra.api.structures.parser.lang.keywords.flow;

import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Keyword;
import com.dfsek.terra.api.structures.tokenizer.Position;

public class ReturnKeyword implements Keyword<Block.ReturnInfo<?>> {
    private final Position position;

    public ReturnKeyword(Position position) {
        this.position = position;
    }

    @Override
    public Block.ReturnInfo<?> apply(ImplementationArguments implementationArguments) {
        return new Block.ReturnInfo<>(Block.ReturnLevel.RETURN, null);
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public ReturnType returnType() {
        return ReturnType.VOID;
    }
}
