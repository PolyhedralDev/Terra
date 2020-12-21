package com.dfsek.terra.api.structures.parser.lang.keywords;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.parser.lang.Keyword;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.tokenizer.Position;

public class IfKeyword implements Keyword<Void> {
    private final Block conditional;
    private final Returnable<Boolean> statement;
    private final Position position;

    public IfKeyword(Block conditional, Returnable<Boolean> statement, Position position) {
        this.conditional = conditional;
        this.statement = statement;
        this.position = position;
    }

    @Override
    public Void apply(Location location) {
        if(statement.apply(location)) conditional.apply(location);
        return null;
    }

    @Override
    public Void apply(Location location, Chunk chunk) {
        if(statement.apply(location, chunk)) conditional.apply(location, chunk);
        return null;
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
