package com.dfsek.terra.minestom;

import com.dfsek.terra.api.platform.block.Block;
import com.dfsek.terra.minestom.world.MinestomBlock;

public final class MinestomAdapter {
    public static net.minestom.server.instance.block.Block adapt(Block block) {
        return ((MinestomBlock) block).getHandle();
    }
}
