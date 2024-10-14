package com.dfsek.terra.allay.handle;

import com.dfsek.terra.allay.JeBlockState;
import com.dfsek.terra.allay.Mapping;
import com.dfsek.terra.allay.delegate.AllayBlockState;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;

/**
 * @author daoge_cmd
 */
public class AllayWorldHandle implements WorldHandle {

    @Override
    public @NotNull BlockState createBlockState(@NotNull String data) {
        JeBlockState jeBlockState = JeBlockState.fromString(data);
        return new AllayBlockState(Mapping.blockStateJeToBe(jeBlockState), jeBlockState);
    }

    @Override
    public @NotNull BlockState air() {
        return AllayBlockState.AIR;
    }

    @Override
    public @NotNull EntityType getEntity(@NotNull String id) {
        return new EntityType() {
            private final Object fakeEntityType = new Object();
            @Override
            public Object getHandle() {
                return fakeEntityType;
            }
        };
    }
}
