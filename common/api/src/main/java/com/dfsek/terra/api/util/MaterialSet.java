package com.dfsek.terra.api.util;

import com.dfsek.terra.api.block.BlockType;

import java.util.Collections;
import java.util.Set;

public interface MaterialSet extends Set<BlockType> {
    static MaterialSet empty() {
        return (MaterialSet) (Object) Collections.emptySet();
    }
}
