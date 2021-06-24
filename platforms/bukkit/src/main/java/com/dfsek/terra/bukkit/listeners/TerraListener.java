package com.dfsek.terra.bukkit.listeners;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.event.EventListener;
import com.dfsek.terra.api.event.annotations.Global;
import com.dfsek.terra.api.event.annotations.Priority;
import com.dfsek.terra.api.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.api.registry.exception.DuplicateEntryException;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.bukkit.world.BukkitTree;
import org.bukkit.TreeType;

public class TerraListener implements EventListener {
    private final TerraPlugin main;

    public TerraListener(TerraPlugin main) {
        this.main = main;
    }

    @Global
    @Priority(Priority.LOWEST)
    public void injectTrees(ConfigPackPreLoadEvent event) {
        for(TreeType value : TreeType.values()) {
            try {
                String id = BukkitAdapter.TREE_TRANSFORMER.translate(value);
                event.getPack().getRegistry(Tree.class).add(id, new BukkitTree(value, main));
                event.getPack().getRegistry(Tree.class).get(id); // Platform trees should never be marked "dead"
            } catch(DuplicateEntryException ignore) { // If another addon has already registered trees, do nothing.
            }
        }
    }
}
