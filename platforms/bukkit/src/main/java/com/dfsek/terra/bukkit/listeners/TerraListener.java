package com.dfsek.terra.bukkit.listeners;

import com.dfsek.terra.api.core.TerraPlugin;
import com.dfsek.terra.api.core.event.EventListener;
import com.dfsek.terra.api.core.event.annotations.Global;
import com.dfsek.terra.api.core.event.events.config.ConfigPackPreLoadEvent;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.bukkit.world.BukkitTree;
import org.bukkit.TreeType;

public class TerraListener implements EventListener {
    private final TerraPlugin main;

    public TerraListener(TerraPlugin main) {
        this.main = main;
    }

    @Global
    public void injectTrees(ConfigPackPreLoadEvent event) {
        for(TreeType value : TreeType.values()) {
            event.getPack().getTreeRegistry().add(BukkitAdapter.TREE_TRANSFORMER.translate(value), new BukkitTree(value, main));
        }
    }
}
