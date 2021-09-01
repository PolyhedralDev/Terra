package com.dfsek.terra.addons.terrascript.parser.lang.keywords.flow;

import java.util.Map;

import com.dfsek.terra.addons.terrascript.parser.lang.Block;
import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Keyword;
import com.dfsek.terra.addons.terrascript.parser.lang.variables.Variable;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public class BreakKeyword implements Keyword<Block.ReturnInfo<?>> {
    private final Position position;
    
    public BreakKeyword(Position position) {
        this.position = position;
    }
    
    @Override
    public Block.ReturnInfo<?> apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
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
