package com.dfsek.terra.addons.structure.structures.parser.lang.keywords.flow;

import com.dfsek.terra.addons.structure.structures.parser.lang.Block;
import com.dfsek.terra.addons.structure.structures.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.structure.structures.parser.lang.Keyword;
import com.dfsek.terra.addons.structure.structures.parser.lang.variables.Variable;
import com.dfsek.terra.addons.structure.structures.tokenizer.Position;

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
