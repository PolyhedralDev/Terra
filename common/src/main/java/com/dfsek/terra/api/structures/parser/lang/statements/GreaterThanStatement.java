package com.dfsek.terra.api.structures.parser.lang.statements;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.lang.Item;
import com.dfsek.terra.api.structures.parser.lang.Statement;
import com.dfsek.terra.api.structures.tokenizer.Position;

public class GreaterThanStatement<T extends Comparable<T>> implements Statement {
    private final Item<T> left;
    private final Item<T> right;
    private final Position position;

    public GreaterThanStatement(Item<T> left, Item<T> right, Position position) {
        this.left = left;
        this.right = right;
        this.position = position;
    }

    @Override
    public Boolean apply(Location location) {
        return left.apply(location).compareTo(right.apply(location)) > 0;
    }

    @Override
    public Boolean apply(Location location, Chunk chunk) {
        return left.apply(location).compareTo(right.apply(location)) > 0;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
