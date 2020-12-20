package com.dfsek.terra.api.structures.parser.lang.statements;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.lang.Item;
import com.dfsek.terra.api.structures.parser.lang.Statement;

public class EqualsStatement implements Statement {
    private final Item<?> left;
    private final Item<?> right;

    public EqualsStatement(Item<?> left, Item<?> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Boolean apply(Location location) {
        return left.apply(location).equals(right.apply(location));
    }

    @Override
    public Boolean apply(Location location, Chunk chunk) {
        return left.apply(location, chunk).equals(right.apply(location, chunk));
    }
}
