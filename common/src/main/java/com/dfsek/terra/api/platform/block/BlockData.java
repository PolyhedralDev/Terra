package com.dfsek.terra.api.platform.block;

import com.dfsek.terra.api.platform.Handle;

public interface BlockData extends Cloneable, Handle {
    MaterialData getMaterial();

    boolean matches(MaterialData materialData);

    BlockData clone();

    String getAsString();
}
