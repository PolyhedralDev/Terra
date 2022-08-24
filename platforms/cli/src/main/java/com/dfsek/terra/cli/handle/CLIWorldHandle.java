package com.dfsek.terra.cli.handle;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.cli.block.CLIBlockState;


public class CLIWorldHandle implements WorldHandle {
    private static final CLIBlockState AIR = new CLIBlockState("minecraft:air");
    
    public static CLIBlockState getAIR() {
        return AIR;
    }
    
    @Override
    public @NonNull BlockState createBlockState(@NonNull String data) {
        return new CLIBlockState(data);
    }
    
    @Override
    public @NonNull BlockState air() {
        return AIR;
    }
    
    @Override
    public @NonNull EntityType getEntity(@NonNull String id) {
        return null;
    }
}
