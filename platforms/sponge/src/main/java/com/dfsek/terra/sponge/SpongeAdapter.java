package com.dfsek.terra.sponge;

import com.dfsek.terra.api.block.BlockState;
import com.dfsek.terra.sponge.world.block.data.SpongeBlockState;

public final class SpongeAdapter {
    public static BlockState adapt(org.spongepowered.api.block.BlockState state) {
        return new SpongeBlockState(state);
    }

    public static org.spongepowered.api.block.BlockState adapt(BlockState data) {
        return ((SpongeBlockState) data).getHandle();
    }
}
