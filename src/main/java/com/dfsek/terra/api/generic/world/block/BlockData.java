package com.dfsek.terra.api.generic.world.block;

import com.dfsek.terra.api.generic.Handle;

public interface BlockData extends Handle, Cloneable {
    MaterialData getMaterial();

    boolean matches(MaterialData materialData);
}
