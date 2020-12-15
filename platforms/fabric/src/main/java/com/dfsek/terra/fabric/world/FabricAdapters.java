package com.dfsek.terra.fabric.world;

import com.dfsek.terra.api.generic.world.vector.Vector3;
import net.minecraft.util.math.BlockPos;

public final class FabricAdapters {
    public static BlockPos fromVector(Vector3 v) {
        return new BlockPos(v.getBlockX(), v.getBlockY(), v.getBlockZ());
    }

    public static Vector3 toVector(BlockPos pos) {
        return new Vector3(pos.getX(), pos.getY(), pos.getZ());
    }
}
