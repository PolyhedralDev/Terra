package com.dfsek.terra.api.structures.parser.lang;

import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.List;

public class Block implements Item<Block.ReturnInfo<?>> {
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
    public synchronized ReturnInfo<?> apply(ImplementationArguments implementationArguments) {
        for(Item<?> item : items) {
            Object result = item.apply(implementationArguments);
            if(result instanceof ReturnInfo) {
                ReturnInfo<?> level = (ReturnInfo<?>) result;
                if(!level.getLevel().equals(ReturnLevel.NONE)) return level;
            }
        }
        return new ReturnInfo<>(ReturnLevel.NONE, null);
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public static class ReturnInfo<T> {
        private final ReturnLevel level;
        private final T data;

        public ReturnInfo(ReturnLevel level, T data) {
            this.level = level;
            this.data = data;
        }

        public ReturnLevel getLevel() {
            return level;
        }

        public T getData() {
            return data;
        }
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
