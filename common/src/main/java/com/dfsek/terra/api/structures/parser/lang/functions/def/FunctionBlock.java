package com.dfsek.terra.api.structures.parser.lang.functions.def;

import com.dfsek.terra.api.structures.parser.lang.Block;
import com.dfsek.terra.api.structures.parser.lang.Item;
import com.dfsek.terra.api.structures.structure.Rotation;
import com.dfsek.terra.api.structures.structure.buffer.Buffer;
import com.dfsek.terra.api.structures.tokenizer.Position;

import java.util.List;
import java.util.Random;

public class FunctionBlock<T> implements Item<Block.ReturnInfo<T>> {
    private final List<Item<?>> items;
    private final Position position;
    private final T defaultVal;

    public FunctionBlock(List<Item<?>> items, T defaultVal, Position position) {
        this.items = items;
        this.position = position;
        this.defaultVal = defaultVal;
    }

    public List<Item<?>> getItems() {
        return items;
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized Block.ReturnInfo<T> apply(Buffer buffer, Rotation rotation, Random random, int recursions) {
        for(Item<?> item : items) {
            Object result = item.apply(buffer, rotation, random, recursions);
            if(result instanceof Block.ReturnInfo) {
                Block.ReturnInfo<T> level = (Block.ReturnInfo<T>) result;
                if(!level.getLevel().equals(Block.ReturnLevel.NONE)) return level;
            }
        }
        return new Block.ReturnInfo<>(Block.ReturnLevel.NONE, defaultVal);
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
