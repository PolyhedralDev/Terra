package com.dfsek.terra.bukkit.listeners;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.math.vector.Location;
import com.dfsek.terra.api.platform.world.Tree;
import com.dfsek.terra.api.platform.world.World;
import com.dfsek.terra.api.transform.MapTransform;
import com.dfsek.terra.api.transform.Transformer;
import com.dfsek.terra.api.util.FastRandom;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.config.pack.WorldConfig;
import com.dfsek.terra.world.TerraWorld;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

/**
 * Listener for events on all implementations.
 */
public class CommonListener implements Listener {
    private final TerraPlugin main;

    public CommonListener(TerraPlugin main) {
        this.main = main;
    }

    private static final Transformer<TreeType, String> TREE_TYPE_STRING_TRANSFORMER = new Transformer.Builder<TreeType, String>()
            .addTransform(new MapTransform<TreeType, String>()
                    .add(TreeType.COCOA_TREE, "JUNGLE_COCOA")
                    .add(TreeType.BIG_TREE, "LARGE_OAK")
                    .add(TreeType.TALL_REDWOOD, "LARGE_SPRUCE")
                    .add(TreeType.REDWOOD, "SPRUCE")
                    .add(TreeType.TREE, "OAK")
                    .add(TreeType.MEGA_REDWOOD, "MEGA_SPRUCE")
                    .add(TreeType.SWAMP, "SWAMP_OAK"))
            .addTransform(TreeType::toString).build();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSaplingGrow(StructureGrowEvent e) {
        if(e.isCancelled()) return;
        World bukkit = BukkitAdapter.adapt(e.getWorld());
        if(!bukkit.isTerraWorld()) return;
        TerraWorld tw = main.getWorld(bukkit);
        WorldConfig c = tw.getConfig();
        if(c.getTemplate().isDisableSaplings()) return;
        e.setCancelled(true);
        Block block = e.getLocation().getBlock();
        BlockData data = block.getBlockData();
        block.setType(Material.AIR);
        Tree tree = c.getTreeRegistry().get(TREE_TYPE_STRING_TRANSFORMER.translate(e.getSpecies()));
        org.bukkit.Location location = e.getLocation();
        if(!tree.plant(new Location(bukkit, location.getX(), location.getY(), location.getZ()), new FastRandom())) block.setBlockData(data);
    }
}
