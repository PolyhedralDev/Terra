package com.dfsek.terra.addons.tree;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.addons.tree.tree.TerraTree;

public class TreeFactory implements ConfigFactory<TreeTemplate, Tree> {
    @Override
    public Tree build(TreeTemplate config, TerraPlugin main) {
        return new TerraTree(config.getSpawnable(), config.getyOffset(), config.getStructures());
    }
}
