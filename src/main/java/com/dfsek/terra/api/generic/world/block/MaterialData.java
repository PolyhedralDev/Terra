package com.dfsek.terra.api.generic.world.block;

import com.dfsek.terra.api.generic.Handle;

public interface MaterialData extends Handle {
    boolean matches(MaterialData other);

    boolean matches(BlockData other);

    boolean isSolid();
}
