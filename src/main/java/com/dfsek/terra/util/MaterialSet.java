package com.dfsek.terra.util;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import java.util.Arrays;
import java.util.HashSet;

public class MaterialSet extends HashSet<Material> {
    private static final long serialVersionUID = 3056512763631017301L;

    public static MaterialSet singleton(Material material) {
        MaterialSet set = new MaterialSet();
        set.add(material);
        return set;
    }

    public static MaterialSet get(Material... materials) {
        MaterialSet set = new MaterialSet();
        set.addAll(Arrays.asList(materials));
        return set;
    }

    public void addTag(String tag) {
        this.addAll(TagUtil.getTag(tag));
    }

    private void add(BlockData data) {
        add(data.getMaterial());
    }
}
