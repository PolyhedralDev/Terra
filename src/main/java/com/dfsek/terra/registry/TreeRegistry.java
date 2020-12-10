package com.dfsek.terra.registry;

import com.dfsek.terra.api.gaea.tree.Tree;
import com.dfsek.terra.api.gaea.tree.TreeType;

public class TreeRegistry extends TerraRegistry<Tree> {
    public TreeRegistry() {
        for(TreeType t : TreeType.values()) add(t.toString(), t); // Populate registry with default trees.
    }
}
