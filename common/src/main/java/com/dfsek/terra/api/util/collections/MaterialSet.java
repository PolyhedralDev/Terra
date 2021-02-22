package com.dfsek.terra.api.util.collections;

import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.platform.block.MaterialData;

import java.util.Arrays;
import java.util.HashSet;

public class MaterialSet extends HashSet<MaterialData> {
    private static final long serialVersionUID = 3056512763631017301L;

    public static MaterialSet singleton(MaterialData material) {
        MaterialSet set = new MaterialSet();
        set.add(material);
        return set;
    }

    public static MaterialSet get(MaterialData... materials) {
        MaterialSet set = new MaterialSet();
        set.addAll(Arrays.asList(materials));
        return set;
    }

    private void add(BlockData data) {
        add(data.getMaterial());
    }
}
