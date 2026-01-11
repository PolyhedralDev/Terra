package com.dfsek.terra.cli.block;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.util.generic.Memo;


public class CLIBlockType implements BlockType {
    private final String value;
    private final boolean solid;
    private final boolean water;
    private final Memo<CLIBlockState> defaultState;

    public CLIBlockType(String value) {
        if(value.contains("[")) throw new IllegalArgumentException("Block Type must not contain properties");
        this.value = value;
        this.solid = !value.equals("minecraft:air");
        this.water = value.equals("minecraft:water");
        this.defaultState = Memo.lazy(() -> new CLIBlockState(value));
    }

    @Override
    public String getHandle() {
        return value;
    }

    @Override
    public BlockState defaultState() {
        return defaultState.value();
    }

    @Override
    public boolean solid() {
        return solid;
    }

    @Override
    public boolean water() {
        return water;
    }
}
