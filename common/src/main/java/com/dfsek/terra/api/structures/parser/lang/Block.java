package com.dfsek.terra.api.structures.parser.lang;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.tokenizer.Position;
import com.dfsek.terra.structure.Rotation;

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
    public Void apply(Location location, Rotation rotation) {
        items.forEach(item -> item.apply(location, rotation));
        return null;
    }

    @Override
    public Void apply(Location location, Chunk chunk, Rotation rotation) {
        items.forEach(item -> item.apply(location, chunk, rotation));
        return null;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
