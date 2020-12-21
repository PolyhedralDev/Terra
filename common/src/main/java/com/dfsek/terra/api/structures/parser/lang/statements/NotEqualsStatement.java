package com.dfsek.terra.api.structures.parser.lang.statements;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.lang.Item;
import com.dfsek.terra.api.structures.parser.lang.Statement;
import com.dfsek.terra.api.structures.tokenizer.Position;

public class NotEqualsStatement implements Statement {
    private final Item<?> left;
    private final Item<?> right;
    private final Position position;

    public NotEqualsStatement(Item<?> left, Item<?> right, Position position) {
        this.left = left;
        this.right = right;
        this.position = position;
    }

    @Override
    public Boolean apply(Location location) {
        return !left.apply(location).equals(right.apply(location));
    }

    @Override
    public Boolean apply(Location location, Chunk chunk) {
        return !left.apply(location, chunk).equals(right.apply(location, chunk));
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
