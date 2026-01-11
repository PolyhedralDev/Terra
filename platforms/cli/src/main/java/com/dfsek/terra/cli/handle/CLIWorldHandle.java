package com.dfsek.terra.cli.handle;

import com.dfsek.terra.api.error.Invalid;
import com.dfsek.terra.api.util.generic.data.types.Either;
import org.jetbrains.annotations.NotNull;

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
    public @NotNull Either<Invalid, BlockState> createBlockState(@NotNull String data) {
        return Either.right(new CLIBlockState(data));
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
