package com.dfsek.terra.addons.terrascript.parser.lang.keywords.flow;

import com.dfsek.terra.addons.terrascript.parser.lang.Block;
import com.dfsek.terra.addons.terrascript.api.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Keyword;
import com.dfsek.terra.addons.terrascript.api.Variable;
import com.dfsek.terra.addons.terrascript.api.Position;

import java.util.Map;

public class FailKeyword implements Keyword<Block.ReturnInfo<?>> {
    private final Position position;

    public FailKeyword(Position position) {
        this.position = position;
    }

    @Override
    public Block.ReturnInfo<?> apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        return new Block.ReturnInfo<>(Block.ReturnLevel.FAIL, null);
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
