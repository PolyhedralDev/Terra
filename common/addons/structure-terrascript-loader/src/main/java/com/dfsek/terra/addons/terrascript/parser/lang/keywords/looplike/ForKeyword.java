package com.dfsek.terra.addons.terrascript.parser.lang.keywords.looplike;

import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Item;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;
import com.dfsek.terra.addons.terrascript.parser.lang.Block;
import com.dfsek.terra.addons.terrascript.parser.lang.Keyword;
import com.dfsek.terra.api.properties.Context;

import java.util.Map;

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
    public Block.ReturnInfo<?> apply(Context context, Map<String, Variable<?>> variableMap) {
        for(initializer.apply(, implementationArguments, variableMap); statement.apply(, implementationArguments, variableMap); incrementer.apply(, implementationArguments, variableMap)) {
            Block.ReturnInfo<?> level = conditional.apply(, implementationArguments, variableMap);
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
