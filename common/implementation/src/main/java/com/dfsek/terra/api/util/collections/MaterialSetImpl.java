package com.dfsek.terra.api.util.collections;

import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.block.BlockType;
import com.dfsek.terra.api.util.MaterialSet;

import java.util.Arrays;
import java.util.HashSet;

public class MaterialSetImpl extends HashSet<BlockType> implements MaterialSet {
    private static final long serialVersionUID = 3056512763631017301L;

    public static MaterialSetImpl singleton(BlockType material) {
        MaterialSetImpl set = new MaterialSetImpl();
        set.add(material);
        return set;
    }

    public static MaterialSetImpl get(BlockType... materials) {
        MaterialSetImpl set = new MaterialSetImpl();
        set.addAll(Arrays.asList(materials));
        return set;
    }

    public static MaterialSetImpl get(BlockState... materials) {
        MaterialSetImpl set = new MaterialSetImpl();
        Arrays.stream(materials).forEach(set::add);
        return set;
    }

    public static MaterialSetImpl empty() {
        return new MaterialSetImpl();
    }

    private void add(BlockState data) {
        add(data.getBlockType());
    }
}
