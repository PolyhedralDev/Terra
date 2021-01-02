package com.dfsek.terra.api.structures.parser.lang.keywords.looplike;

import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.api.structures.parser.lang.Item;
import com.dfsek.terra.api.structures.parser.lang.Keyword;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.tokenizer.Position;

public class ForKeyword implements Keyword<Block.ReturnInfo<?>> {
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
    public Block.ReturnInfo<?> apply(ImplementationArguments implementationArguments) {
        for(initializer.apply(implementationArguments); statement.apply(implementationArguments); incrementer.apply(implementationArguments)) {
            Block.ReturnInfo<?> level = conditional.apply(implementationArguments);
            if(level.getLevel().equals(Block.ReturnLevel.BREAK)) break;
            if(level.getLevel().isReturnFast()) return level;
        }
        return new Block.ReturnInfo<>(Block.ReturnLevel.NONE, null);
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
