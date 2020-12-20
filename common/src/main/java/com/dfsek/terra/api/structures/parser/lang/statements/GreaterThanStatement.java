package com.dfsek.terra.api.structures.parser.lang.statements;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.parser.lang.Item;
import com.dfsek.terra.api.structures.parser.lang.Statement;

public class GreaterThanStatement<T extends Comparable<T>> implements Statement {
    private final Item<T> left;
    private final Item<T> right;

    public GreaterThanStatement(Item<T> left, Item<T> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Boolean apply(Location location) {
        return left.apply(location).compareTo(right.apply(location)) > 0;
    }

    @Override
    public Boolean apply(Location location, Chunk chunk) {
        return left.apply(location).compareTo(right.apply(location)) > 0;
    }
}
