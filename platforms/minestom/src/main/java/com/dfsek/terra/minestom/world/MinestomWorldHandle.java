package com.dfsek.terra.minestom.world;

import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;
import com.dfsek.terra.minestom.block.MinestomBlockState;
import com.dfsek.terra.minestom.entity.MinestomEntityType;


public class MinestomWorldHandle implements WorldHandle {
    private static final MinestomBlockState AIR = new MinestomBlockState(Block.AIR);

    @Override
    public @NotNull BlockState createBlockState(@NotNull String data) {
        return MinestomBlockState.fromStateId(data);
    }

    @Override
    public @NotNull BlockState air() {
        return AIR;
    }

    @Override
    public @NotNull EntityType getEntity(@NotNull String id) {
        return new MinestomEntityType(id);
    }
}
