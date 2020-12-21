package com.dfsek.terra.api.structures.parser.lang;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.List;

public class Block implements Item<Void> {
    private final List<Item<?>> items;
    private final Position position;

    public Block(List<Item<?>> items, Position position) {
        this.items = items;
        this.position = position;
    }

    public List<Item<?>> getItems() {
        return items;
    }

    @Override
    public Void apply(Location location) {
        items.forEach(item -> item.apply(location));
        return null;
    }

    @Override
    public Void apply(Location location, Chunk chunk) {
        items.forEach(item -> item.apply(location, chunk));
        return null;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
