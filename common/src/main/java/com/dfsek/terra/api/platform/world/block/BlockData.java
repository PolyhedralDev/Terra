package com.dfsek.terra.api.platform.world.block;

import com.dfsek.terra.api.platform.Handle;

public interface BlockData extends Cloneable, Handle {
    MaterialData getMaterial();

    boolean matches(MaterialData materialData);

    BlockData clone();
}
