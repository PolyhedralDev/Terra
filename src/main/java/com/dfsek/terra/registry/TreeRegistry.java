package com.dfsek.terra.registry;

import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.tree.TreeType;

public class TreeRegistry extends TerraRegistry<Tree> {
    public TreeRegistry() {
        for(TreeType t : TreeType.values()) add(t.toString(), t); // Populate registry with default trees.
    }
}
