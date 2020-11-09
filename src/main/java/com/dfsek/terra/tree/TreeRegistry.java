package com.dfsek.terra.tree;

import org.polydev.gaea.tree.Tree;
import org.polydev.gaea.tree.TreeType;

import java.util.HashMap;
import java.util.Map;

public class TreeRegistry {
    private final Map<String, Tree> trees = new HashMap<>();

    public TreeRegistry() {
        for(TreeType t : TreeType.values()) trees.put(t.toString(), t); // Populate registry with default trees.
    }

    /**
     * Add a tree to the registry with a name.
     *
     * @param name  Name of the tree.
     * @param value Tree to add
     * @return True if tree was overwritten.
     */
    public boolean add(String name, Tree value) {
        boolean exists = trees.containsKey(name);
        trees.put(name, value);
        return exists;
    }

    public boolean contains(String name) {
        return trees.containsKey(name);
    }

    public Tree get(String id) {
        return trees.get(id);
    }
}
