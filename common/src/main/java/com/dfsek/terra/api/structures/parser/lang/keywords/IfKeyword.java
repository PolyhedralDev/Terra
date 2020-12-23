package com.dfsek.terra.api.structures.parser.lang.keywords;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.parser.lang.Keyword;
import com.dfsek.terra.api.structures.parser.lang.Returnable;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.tokenizer.Position;

public class IfKeyword implements Keyword<Block.ReturnLevel> {
    private final Block conditional;
    private final Returnable<Boolean> statement;
    private final Position position;

    public IfKeyword(Block conditional, Returnable<Boolean> statement, Position position) {
        this.conditional = conditional;
        this.statement = statement;
        this.position = position;
    }

    @Override
    public Block.ReturnLevel apply(Location location, Rotation rotation, int recursions) {
        if(statement.apply(location, rotation, recursions)) return conditional.apply(location, rotation, recursions);
        return Block.ReturnLevel.NONE;
    }

    @Override
    public Block.ReturnLevel apply(Location location, Chunk chunk, Rotation rotation, int recursions) {
        if(statement.apply(location, chunk, rotation, recursions)) return conditional.apply(location, chunk, rotation, recursions);
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
