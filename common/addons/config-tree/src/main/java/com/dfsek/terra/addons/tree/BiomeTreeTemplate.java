package com.dfsek.terra.addons.tree;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.addons.tree.tree.TreeLayer;

import java.util.Collections;
import java.util.List;

public class BiomeTreeTemplate implements ConfigTemplate {
    @Value("trees")
    @Default
    private List<TreeLayer> trees = Collections.emptyList();

    public List<TreeLayer> getTrees() {
        return trees;
    }
}
