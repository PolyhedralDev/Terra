package com.dfsek.terra.addons.terrascript.parser.lang;

import com.dfsek.terra.addons.terrascript.api.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.api.Position;
import com.dfsek.terra.addons.terrascript.api.lang.Item;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;
import com.dfsek.terra.api.properties.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public ReturnInfo<?> apply(ImplementationArguments implementationArguments) {
        return apply(, implementationArguments, new HashMap<>());
    }

    @Override
    public ReturnInfo<?> apply(Context context, Map<String, Variable<?>> variableMap) {
        Map<String, Variable<?>> scope = new HashMap<>(variableMap);
        for(Item<?> item : items) {
            Object result = item.apply(, implementationArguments, scope);
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
}
