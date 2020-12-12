package com.dfsek.terra.registry;

import com.dfsek.terra.api.gaea.tree.Tree;
import com.dfsek.terra.api.generic.TerraPlugin;

public class TreeRegistry extends TerraRegistry<Tree> {
    private final TerraPlugin main;

    public TreeRegistry(TerraPlugin main) {
        this.main = main;
    }

    private void addTree(String id) {
        try {
            add(id, main.getWorldHandle().getTree(id));
        } catch(IllegalArgumentException e) {
            main.getLogger().warning("Unable to load tree " + id + ": " + e.getMessage());
        }
    }
}
