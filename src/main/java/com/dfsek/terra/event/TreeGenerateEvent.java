package com.dfsek.terra.event;

import com.dfsek.terra.TerraWorld;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.polydev.gaea.tree.Tree;

public class TreeGenerateEvent extends TerraWorldEvent implements Cancellable {
    private boolean cancelled;
    private Tree tree;

    public TreeGenerateEvent(TerraWorld tw, Location l, Tree tree) {
        super(tw, l);
        this.tree = tree;
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
