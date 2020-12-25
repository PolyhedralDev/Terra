package com.dfsek.terra.api.structures.parser.lang;

import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
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
    public synchronized ReturnLevel apply(Buffer buffer, Rotation rotation, int recursions) {
        for(Item<?> item : items) {
            Object result = item.apply(buffer, rotation, recursions);
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
        NONE(false), BREAK(false), CONTINUE(false), RETURN(true), FAIL(true);

        private final boolean returnFast;

        ReturnLevel(boolean returnFast) {
            this.returnFast = returnFast;
        }

        public boolean isReturnFast() {
            return returnFast;
        }
    }
}
