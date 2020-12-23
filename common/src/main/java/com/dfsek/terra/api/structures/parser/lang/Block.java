package com.dfsek.terra.api.structures.parser.lang;

import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Chunk;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.List;

public class Block implements Item<Block.ReturnLevel> {
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
    public ReturnLevel apply(Location location, Rotation rotation) {
        for(Item<?> item : items) {
            Object result = item.apply(location, rotation);
            if(result instanceof ReturnLevel) {
                ReturnLevel level = (ReturnLevel) result;
                if(!level.equals(ReturnLevel.NONE)) return level;
            }
        }
        return ReturnLevel.NONE;
    }

    @Override
    public ReturnLevel apply(Location location, Chunk chunk, Rotation rotation) {
        for(Item<?> item : items) {
            Object result = item.apply(location, chunk, rotation);
            if(result instanceof ReturnLevel) {
                ReturnLevel level = (ReturnLevel) result;
                if(!level.equals(ReturnLevel.NONE)) return level;
            }
        }
        return ReturnLevel.NONE;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public enum ReturnLevel {
        NONE, BREAK, CONTINUE, RETURN
    }
}
