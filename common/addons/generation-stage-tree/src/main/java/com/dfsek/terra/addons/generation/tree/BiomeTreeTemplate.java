package com.dfsek.terra.addons.generation.tree;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.loading.object.ObjectTemplate;

import java.util.Collections;
import java.util.List;

public class BiomeTreeTemplate implements ObjectTemplate<BiomeTrees> {
    @Value("trees")
    @Default
    private List<TreeLayer> trees = Collections.emptyList();

    @Override
    public BiomeTrees get() {
        return new BiomeTrees(trees);
    }
}
