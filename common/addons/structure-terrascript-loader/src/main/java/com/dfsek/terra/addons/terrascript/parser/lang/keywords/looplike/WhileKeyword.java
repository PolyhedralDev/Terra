package com.dfsek.terra.addons.terrascript.parser.lang.keywords.looplike;

import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Returnable;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;
import com.dfsek.terra.addons.terrascript.parser.lang.Block;
import com.dfsek.terra.addons.terrascript.parser.lang.Keyword;
import com.dfsek.terra.api.properties.Context;

import java.util.Map;

public class WhileKeyword implements Keyword<Block.ReturnInfo<?>> {
    private final Block conditional;
    private final Returnable<Boolean> statement;
    private final Position position;

    public WhileKeyword(Block conditional, Returnable<Boolean> statement, Position position) {
        this.conditional = conditional;
        this.statement = statement;
        this.position = position;
    }

    @Override
    public Block.ReturnInfo<?> apply(Context context, Map<String, Variable<?>> variableMap) {
        while(statement.apply(context, variableMap)) {
            Block.ReturnInfo<?> level = conditional.apply(context, variableMap);
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
