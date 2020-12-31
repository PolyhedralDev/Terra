package com.dfsek.terra.api.structures.parser.lang.keywords.flow;

import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.parser.lang.Keyword;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.Random;

public class BreakKeyword implements Keyword<Block.ReturnInfo<?>> {
    private final Position position;

    public BreakKeyword(Position position) {
        this.position = position;
    }

    @Override
    public Block.ReturnInfo<?> apply(Buffer buffer, Rotation rotation, Random random, int recursions) {
        return new Block.ReturnInfo<>(Block.ReturnLevel.BREAK, null);
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
