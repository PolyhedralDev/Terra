package com.dfsek.terra.bukkit.listeners;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.WorldConfig;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.Tree;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.transform.MapTransform;
import com.dfsek.terra.transform.TransformerImpl;
import com.dfsek.terra.util.FastRandom;


/**
 * Listener for events on all implementations.
 */
public class CommonListener implements Listener {
    private static final TransformerImpl<TreeType, String> TREE_TYPE_STRING_TRANSFORMER = new TransformerImpl.Builder<TreeType, String>()
            .addTransform(new MapTransform<TreeType, String>()
                                  .add(TreeType.COCOA_TREE, "JUNGLE_COCOA")
                                  .add(TreeType.BIG_TREE, "LARGE_OAK")
                                  .add(TreeType.TALL_REDWOOD, "LARGE_SPRUCE")
                                  .add(TreeType.REDWOOD, "SPRUCE")
                                  .add(TreeType.TREE, "OAK")
                                  .add(TreeType.MEGA_REDWOOD, "MEGA_SPRUCE")
                                  .add(TreeType.SWAMP, "SWAMP_OAK"))
            .addTransform(TreeType::toString).build();
    private final TerraPlugin main;
    
    public CommonListener(TerraPlugin main) {
        this.main = main;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSaplingGrow(StructureGrowEvent e) {
        if(e.isCancelled()) return;
        World bukkit = BukkitAdapter.adapt(e.getWorld());
        WorldConfig c = bukkit.getConfig();
        if(c.isDisableSaplings()) return;
        e.setCancelled(true);
        Block block = e.getLocation().getBlock();
        BlockData data = block.getBlockData();
        block.setType(Material.AIR);
        Tree tree = c.getRegistry(Tree.class).get(TREE_TYPE_STRING_TRANSFORMER.translate(e.getSpecies()));
        org.bukkit.Location location = e.getLocation();
        if(!tree.plant(new Vector3(location.getX(), location.getY(), location.getZ()), BukkitAdapter.adapt(e.getWorld()), new FastRandom()))
            block.setBlockData(data);
    }
}
