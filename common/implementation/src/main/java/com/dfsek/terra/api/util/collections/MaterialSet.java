package com.dfsek.terra.api.util.collections;

import com.dfsek.terra.api.block.BlockData;
import com.dfsek.terra.api.block.BlockType;

import java.util.Arrays;
import java.util.HashSet;

public class MaterialSet extends HashSet<BlockType> {
    private static final long serialVersionUID = 3056512763631017301L;

    public static MaterialSet singleton(BlockType material) {
        MaterialSet set = new MaterialSet();
        set.add(material);
        return set;
    }

    public static MaterialSet get(BlockType... materials) {
        MaterialSet set = new MaterialSet();
        set.addAll(Arrays.asList(materials));
        return set;
    }

    public static MaterialSet get(BlockData... materials) {
        MaterialSet set = new MaterialSet();
        Arrays.stream(materials).forEach(set::add);
        return set;
    }

    public static MaterialSet empty() {
        return new MaterialSet();
    }

    private void add(BlockData data) {
        add(data.getBlockType());
    }
}
