package com.dfsek.terra.config.factories;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigFactory;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.config.templates.TreeTemplate;
import com.dfsek.terra.world.population.items.tree.TerraTree;

public class TreeFactory implements ConfigFactory<TreeTemplate, Tree> {
    @Override
    public Tree build(TreeTemplate config, TerraPlugin main) {
        return new TerraTree(config.getSpawnable(), config.getyOffset(), config.getStructures());
    }
}
