package com.dfsek.terra.api.structures.parser.lang.keywords.looplike;

import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.parser.lang.Item;
import com.dfsek.terra.api.structures.parser.lang.Keyword;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.Random;

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
    public Block.ReturnLevel apply(Buffer buffer, Rotation rotation, Random random, int recursions) {
        for(initializer.apply(buffer, rotation, random, recursions); statement.apply(buffer, rotation, random, recursions); incrementer.apply(buffer, rotation, random, recursions)) {
            Block.ReturnLevel level = conditional.apply(buffer, rotation, random, recursions);
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
