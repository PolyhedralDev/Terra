package com.dfsek.terra.api.structures.parser.lang.keywords.looplike;

import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.parser.lang.Item;
import com.dfsek.terra.api.structures.parser.lang.Keyword;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.tokenizer.Position;

public class ForKeyword implements Keyword<Block.ReturnLevel> {
    private final Block conditional;
    private final Item<?> initializer;
    private final Returnable<Boolean> statement;
    private final Item<?> incrementer;
    private final Position position;

    public ForKeyword(Block conditional, Item<?> initializer, Returnable<Boolean> statement, Item<?> incrementer, Position position) {
        this.conditional = conditional;
        this.initializer = initializer;
        this.statement = statement;
        this.incrementer = incrementer;
        this.position = position;
    }

    @Override
    public Block.ReturnLevel apply(Buffer buffer, Rotation rotation, int recursions) {
        for(initializer.apply(buffer, rotation, recursions); statement.apply(buffer, rotation, recursions); incrementer.apply(buffer, rotation, recursions)) {
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
