package com.dfsek.terra.allay.handle;

import com.dfsek.terra.api.error.Invalid;
import com.dfsek.terra.api.error.InvalidBlockStateError;
import com.dfsek.terra.api.util.generic.data.types.Either;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.allay.JeBlockState;
import com.dfsek.terra.allay.Mapping;
import com.dfsek.terra.allay.delegate.AllayBlockState;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;


/**
 * @author daoge_cmd
 */
public class AllayWorldHandle implements WorldHandle {

    @Override
    public @NotNull Either<Invalid, BlockState> createBlockState(@NotNull String data) {
        try {
            JeBlockState jeBlockState = JeBlockState.fromString(data);
            return Either.right(new AllayBlockState(Mapping.blockStateJeToBe(jeBlockState), jeBlockState));
        } catch(Exception e) {
            return new InvalidBlockStateError(e).left();
        }
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
