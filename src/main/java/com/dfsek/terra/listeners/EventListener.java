package com.dfsek.terra.listeners;

import com.dfsek.terra.api.implementations.bukkit.TerraBukkitPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

/**
 * Listener for events on all implementations.
 */
public class EventListener implements Listener {
    private final TerraBukkitPlugin main;

    public EventListener(TerraBukkitPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onSaplingGrow(StructureGrowEvent e) {
        /*
        World bukkit = new BukkitWorld(e.getWorld());
        if(!TerraWorld.isTerraWorld(bukkit)) return;
        TerraWorld tw = main.getWorld(bukkit);
        ConfigPack c = tw.getConfig();
        if(c.getTemplate().isDisableSaplings()) return;
        e.setCancelled(true);
        Block block = e.getLocation().getBlock();
        BlockData data = block.getBlockData();
        block.setType(Material.AIR);
        TreeRegistry registry = c.getTreeRegistry();
        Tree tree = registry.get(TreeType.fromBukkit(e.getSpecies()).toString());
        Debug.info("Overriding tree type: " + e.getSpecies());
        if(tree instanceof TerraTree) {
            if(!((TerraTree) tree).plantBlockCheck(e.getLocation().subtract(0, 1, 0), new FastRandom(), main)) {
                block.setBlockData(data);
            }
        } else if(!tree.plant(e.getLocation().subtract(0, 1, 0), new FastRandom(), main)) block.setBlockData(data);

         */
    }
}
