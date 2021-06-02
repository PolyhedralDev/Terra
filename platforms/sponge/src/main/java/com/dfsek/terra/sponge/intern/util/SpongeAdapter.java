package com.dfsek.terra.sponge.intern.util;

import com.dfsek.terra.api.math.vector.Vector3;
import net.minecraft.core.BlockPos;

public final class SpongeAdapter {
    public static Vector3 adapt(BlockPos pos) {
        return new Vector3(pos.getX(), pos.getY(), pos.getZ());
    }

    public static BlockPos adapt(Vector3 vec) {
        return new BlockPos(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
    }
}
