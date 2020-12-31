package com.dfsek.terra.api.structures.parser.lang.keywords.flow;

import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.parser.lang.Keyword;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.Random;

public class ReturnKeyword implements Keyword<Block.ReturnInfo<?>> {
    private final Position position;

    public ReturnKeyword(Position position) {
        this.position = position;
    }

    @Override
    public Block.ReturnInfo<?> apply(Buffer buffer, Rotation rotation, Random random, int recursions) {
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
