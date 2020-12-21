package com.dfsek.terra.api.structures.parser.lang.keywords;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.lang.Keyword;
import com.dfsek.terra.api.structures.tokenizer.Position;

public class ReturnKeyword implements Keyword<Void> {
    private final Position position;

    public ReturnKeyword(Position position) {
        this.position = position;
    }

    @Override
    public Void apply(Location location) {
        return null;
    }

    @Override
    public Void apply(Location location, Chunk chunk) {
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
