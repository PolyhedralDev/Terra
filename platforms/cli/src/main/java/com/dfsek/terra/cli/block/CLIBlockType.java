package com.dfsek.terra.cli.block;

import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.util.generic.Lazy;


public class CLIBlockType implements BlockType {
    private final String value;
    private final boolean solid;
    private final boolean water;
    private final Lazy<CLIBlockState> defaultState;
    
    public CLIBlockType(String value) {
        if(value.contains("[")) throw new IllegalArgumentException("Block Type must not contain properties");
        this.value = value;
        this.solid = !value.equals("minecraft:air");
        this.water = value.equals("minecraft:water");
        this.defaultState = Lazy.lazy(() -> new CLIBlockState(value));
    }
    
    @Override
    public String getHandle() {
        return value;
    }
    
    @Override
    public BlockState getDefaultState() {
        return defaultState.value();
    }
    
    @Override
    public boolean isSolid() {
        return solid;
    }
    
    @Override
    public boolean isWater() {
        return water;
    }
}
