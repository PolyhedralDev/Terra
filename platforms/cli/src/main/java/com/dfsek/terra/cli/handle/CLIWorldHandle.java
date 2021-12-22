package com.dfsek.terra.cli.handle;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;

import com.dfsek.terra.cli.block.CLIBlockState;

import org.jetbrains.annotations.NotNull;


public class CLIWorldHandle implements WorldHandle {
    private static final CLIBlockState AIR = new CLIBlockState("minecraft:air");
    @Override
    public @NotNull BlockState createBlockState(@NotNull String data) {
        return new CLIBlockState(data);
    }
    
    @Override
    public @NotNull BlockState air() {
        return AIR;
    }
    
    @Override
    public @NotNull EntityType getEntity(@NotNull String id) {
        return null;
    }
}
