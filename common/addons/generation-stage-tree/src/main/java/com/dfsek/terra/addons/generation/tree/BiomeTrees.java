package com.dfsek.terra.addons.generation.tree;

import com.dfsek.terra.api.properties.Properties;

import java.util.List;

public class BiomeTrees implements Properties {
    private final List<TreeLayer> trees;

    public BiomeTrees(List<TreeLayer> trees) {
        this.trees = trees;
    }

    public List<TreeLayer> getTrees() {
        return trees;
    }
}
