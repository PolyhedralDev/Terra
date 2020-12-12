package com.dfsek.terra.api.generic.world.block;

import com.dfsek.terra.api.generic.Handle;

public interface BlockData extends Cloneable, Handle {
    MaterialData getMaterial();

    boolean matches(MaterialData materialData);

    BlockData clone();
}
