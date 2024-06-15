package org.allaymc.terra.allay.handle;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;

import org.allaymc.terra.allay.JeBlockState;
import org.allaymc.terra.allay.Mapping;
import org.allaymc.terra.allay.delegate.AllayBlockState;
import org.allaymc.terra.allay.delegate.AllayEntityTypeHandle;
import org.jetbrains.annotations.NotNull;


/**
 * Terra Project 2024/6/16
 *
 * @author daoge_cmd
 */
public class AllayWorldHandle implements WorldHandle {

    @Override
    public @NotNull BlockState createBlockState(@NotNull String data) {
        var jeBlockState = JeBlockState.fromString(data);
        return new AllayBlockState(Mapping.blockStateJeToBe(jeBlockState), jeBlockState);
    }

    @Override
    public @NotNull BlockState air() {
        return AllayBlockState.AIR;
    }

    @Override
    public @NotNull EntityType getEntity(@NotNull String id) {
        return new AllayEntityTypeHandle(id);
    }
}
