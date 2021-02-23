package com.dfsek.terra.sponge;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.sponge.world.block.data.SpongeBlockData;
import org.spongepowered.api.block.BlockState;

public final class SpongeAdapter {
    public static BlockData adapt(BlockState state) {
        return new SpongeBlockData(state);
    }

    public static BlockState adapt(BlockData data) {
        return ((SpongeBlockData) data).getHandle();
    }
}
