package com.dfsek.terra.addons.terrascript.parser.lang.functions.def;

import com.dfsek.terra.addons.terrascript.parser.lang.Block;
import com.dfsek.terra.addons.terrascript.api.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.api.lang.Item;
import com.dfsek.terra.addons.terrascript.api.lang.Variable;
import com.dfsek.terra.addons.terrascript.api.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionBlock<T> implements Item<T> {
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
    public synchronized T apply(ImplementationArguments implementationArguments, Map<String, Variable<?>> variableMap) {
        Map<String, Variable<?>> scope = new HashMap<>(variableMap);
        for(Item<?> item : items) {
            Object result = item.apply(implementationArguments, variableMap);
            if(result instanceof Block.ReturnInfo) {
                Block.ReturnInfo<T> level = (Block.ReturnInfo<T>) result;
                if(level.getLevel().equals(Block.ReturnLevel.RETURN)) return level.getData();
            }
        }
        return defaultVal;
    }

    @Override
    public Position getPosition() {
        return position;
    }
}
