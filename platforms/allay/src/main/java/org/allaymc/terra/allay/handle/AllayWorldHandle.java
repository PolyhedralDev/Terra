package org.allaymc.terra.allay.handle;

import org.allaymc.terra.allay.JeBlockState;
import org.allaymc.terra.allay.Mapping;
import org.allaymc.terra.allay.delegate.AllayBlockState;
import org.jetbrains.annotations.NotNull;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.handle.WorldHandle;


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
        // TODO: 我们暂时不支持实体，因为端本身都没实体ai，生成实体没有意义
        return new EntityType() {
            private final Object fakeEntityType = new Object();
            @Override
            public Object getHandle() {
                return fakeEntityType;
            }
        };
    }
}
