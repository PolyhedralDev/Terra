package com.dfsek.terra.api.structures.parser.lang.keywords.looplike;

import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.parser.lang.Keyword;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.tokenizer.Position;

public class WhileKeyword implements Keyword<Block.ReturnLevel> {
    private final Block conditional;
    private final Returnable<Boolean> statement;
    private final Position position;

    public WhileKeyword(Block conditional, Returnable<Boolean> statement, Position position) {
        this.conditional = conditional;
        this.statement = statement;
        this.position = position;
    }

    @Override
    public Block.ReturnLevel apply(Buffer buffer, Rotation rotation, int recursions) {
        while(statement.apply(buffer, rotation, recursions)) {
            Block.ReturnLevel level = conditional.apply(buffer, rotation, recursions);
            if(level.equals(Block.ReturnLevel.BREAK)) break;
            if(level.isReturnFast()) return level;
        }
        return Block.ReturnLevel.NONE;
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
