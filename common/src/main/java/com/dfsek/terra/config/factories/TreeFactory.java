package com.dfsek.terra.config.factories;

import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.api.world.tree.Tree;
import com.dfsek.terra.config.templates.TreeTemplate;
import com.dfsek.terra.generation.items.tree.TerraTree;

public class TreeFactory implements TerraFactory<TreeTemplate, Tree> {
    @Override
    public Tree build(TreeTemplate config, TerraPlugin main) throws LoadException {
        return new TerraTree(config.getSpawnable(), config.getyOffset(), config.getStructures());
    }
}
