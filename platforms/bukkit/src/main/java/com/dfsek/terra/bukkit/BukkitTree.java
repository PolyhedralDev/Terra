package com.dfsek.terra.bukkit;

import com.dfsek.terra.api.generic.Tree;
import org.bukkit.TreeType;

public class BukkitTree implements Tree {
    private final TreeType delegate;

    public BukkitTree(TreeType delegate) {
        this.delegate = delegate;
    }

    @Override
    public TreeType getHandle() {
        return delegate;
    }
}
