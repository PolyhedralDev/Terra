package com.dfsek.terra;

import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.polydev.gaea.GaeaPlugin;

public class EventListener implements Listener {
    private final GaeaPlugin main;

    public EventListener(GaeaPlugin main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEnderEye(EntitySpawnEvent e) {
        /*
        Entity entity = e.getEntity();
        if(e.getEntityType().equals(EntityType.ENDER_SIGNAL)) {
            Debug.info("Detected Ender Signal...");
            TerraWorld tw = TerraWorld.getWorld(e.getEntity().getWorld());
            EnderSignal signal = (EnderSignal) entity;
            StructureTemplate config = tw.getConfig().getTemplate().getStructureLocatables().get(StructureTypeEnum.STRONGHOLD);
            if(config != null) {
                Debug.info("Overriding Ender Signal...");
                AsyncStructureFinder finder = new AsyncStructureFinder(tw.getGrid(), config, e.getLocation(), 0, 500, location -> {
                    if(location != null) signal.setTargetLocation(location.toLocation(signal.getWorld()));
                    Debug.info("Location: " + location);
                });
                finder.run(); // Do this synchronously so eye doesn't change direction several ticks after spawning.
            } else
                main.getLogger().warning("No overrides are defined for Strongholds. Ender Signals will not work correctly.");
        }
         */
        // TODO: implementation
    }

    @EventHandler
    public void onCartographerChange(VillagerAcquireTradeEvent e) {
        if(!(e.getEntity() instanceof Villager)) return;
        if(((Villager) e.getEntity()).getProfession().equals(Villager.Profession.CARTOGRAPHER))
            e.setCancelled(true); // Cancel leveling if the villager is a Cartographer, to prevent crashing server.
    }

    @EventHandler
    public void onCartographerLevel(VillagerCareerChangeEvent e) {
        if(e.getProfession().equals(Villager.Profession.CARTOGRAPHER)) {
            e.getEntity().setProfession(Villager.Profession.NITWIT); // Give villager new profession to prevent server crash.
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSaplingGrow(StructureGrowEvent e) {
        /*
        if(!TerraWorld.isTerraWorld(e.getWorld())) return;
        TerraWorld tw = TerraWorld.getWorld(e.getWorld());
        ConfigPack c = tw.getConfig();
        if(c.preventSaplingOverride) return;
        e.setCancelled(true);
        Block block = e.getLocation().getBlock();
        BlockData data = block.getBlockData();
        block.setType(Material.AIR);
        TreeRegistry registry = c.getTreeRegistry();
        Tree tree = registry.get(TreeType.fromBukkit(e.getSpecies()).toString());
        Debug.info("Overriding tree type: " + e.getSpecies());
        if(tree instanceof TerraTree) {
            if(!tree.plant(e.getLocation(), new FastRandom(), Terra.getInstance())) {
                block.setBlockData(data);
            }
        } else if(!tree.plant(e.getLocation(), new FastRandom(), Terra.getInstance())) block.setBlockData(data);
        */
        // TODO: implementation
    }
}
