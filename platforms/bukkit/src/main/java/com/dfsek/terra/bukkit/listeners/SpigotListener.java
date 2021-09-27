package com.dfsek.terra.bukkit.listeners;

import com.dfsek.terra.api.Platform;

import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerCareerChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Listener to load on Spigot servers, contains Villager crash prevention and hacky ender eye redirection.
 * <p>
 * (This is currently loaded on all servers; once Paper accepts the StructureLocateEvent PR this will only be loaded on servers without
 * StructureLocateEvent).
 */
public class SpigotListener implements Listener {
    private final Platform platform;
    private static final Logger logger = LoggerFactory.getLogger(SpigotListener.class);
    
    public SpigotListener(Platform platform) {
        this.platform = platform;
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEnderEye(EntitySpawnEvent e) {
/*
        Entity entity = e.getEntity();
        if(e.getEntityType() == EntityType.ENDER_SIGNAL) {
            logger.info("Detected Ender Signal...");
            World w = BukkitAdapter.adapt(e.getEntity().getWorld());
            EnderSignal signal = (EnderSignal) entity;
            ConfiguredStructure config = tw.getConfig().getRegistry(TerraStructure.class).get(w.getConfig().getLocatable().get
            ("STRONGHOLD"));
            if(config != null) {
                logger.info("Overriding Ender Signal...");
                AsyncStructureFinder finder = new AsyncStructureFinder(tw.getBiomeProvider(), config, BukkitAdapter.adapt(e.getLocation()
                .toVector()), tw.getWorld(), 0, 500, location -> {
                    if(location != null)
                        signal.setTargetLocation(BukkitAdapter.adapt(location).toLocation(e.getLocation().getWorld()));
                    logger.info("Location: {}", location);
                }, main);
                finder.run(); // Do this synchronously so eye doesn't change direction several ticks after spawning.
            } else
                logger.warn("No overrides are defined for Strongholds. Ender Signals will not work correctly.");
        }
*/
    }
    
    @EventHandler
    public void onCartographerChange(VillagerAcquireTradeEvent e) {
        if(!(e.getEntity() instanceof Villager))
            return;
        if(((Villager) e.getEntity()).getProfession() == Villager.Profession.CARTOGRAPHER) {
            logger.error("""
                         .------------------------------------------------------------------------.
                         |     Prevented server crash by stopping Cartographer villager from      |
                         |  spawning. Please upgrade to Paper, which has a StructureLocateEvent   |
                         |   that fixes this issue at the source, and doesn't require us to do    |
                         |                           stupid band-aids.                            |
                         |------------------------------------------------------------------------|
                         """);
            e.setCancelled(true); // Cancel leveling if the villager is a Cartographer, to prevent crashing server.
        }
    }
    
    @EventHandler
    public void onCartographerLevel(VillagerCareerChangeEvent e) {
        if(e.getProfession() == Villager.Profession.CARTOGRAPHER) {
            logger.error("""
                         .------------------------------------------------------------------------.
                         | Prevented server crash by stopping Cartographer villager from leveling |
                         |   up. Please upgrade to Paper, which has a StructureLocateEvent that   |
                         |  fixes this issue at the source, and doesn't require us to do stupid   |
                         |                               band-aids.                               |
                         |------------------------------------------------------------------------|
                         """);
            e.getEntity().setProfession(Villager.Profession.NITWIT); // Give villager new profession to prevent server crash.
            e.setCancelled(true);
        }
    }
}
