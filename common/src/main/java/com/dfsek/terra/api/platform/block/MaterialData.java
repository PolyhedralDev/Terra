package com.dfsek.terra.api.platform.block;

import com.dfsek.terra.api.platform.Handle;

public interface MaterialData extends Handle {
    boolean matches(MaterialData other);

    boolean matches(BlockData other);

    boolean isSolid();

    boolean isAir();

    double getMaxDurability();

    BlockData createBlockData();
}
