/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.bukkit.listeners;

import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.entity.VillagerCareerChangeEvent;

import com.dfsek.terra.api.Platform;


/**
 * Listener to load on Spigot servers, contains Villager crash prevention and hacky ender eye redirection.
 * <p>
 * (This is currently loaded on all servers; once Paper accepts the StructureLocateEvent PR this will only be loaded on servers without
 * StructureLocateEvent).
 */
public class SpigotListener implements Listener {
    private final Platform platform;
    
    public SpigotListener(Platform platform) {
        this.platform = platform;
    }
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onEnderEye(EntitySpawnEvent e) {
        /*
        Entity entity = e.getEntity();
        if(e.getEntityType().equals(EntityType.ENDER_SIGNAL)) {
            main.getDebugLogger().info("Detected Ender Signal...");
            World w = BukkitAdapter.adapt(e.getEntity().getWorld());
            EnderSignal signal = (EnderSignal) entity;
            ConfiguredStructure config = tw.getConfig().getRegistry(TerraStructure.class).get(w.getConfig().getLocatable().get
            ("STRONGHOLD"));
            if(config != null) {
                main.getDebugLogger().info("Overriding Ender Signal...");
                AsyncStructureFinder finder = new AsyncStructureFinder(tw.getBiomeProvider(), config, BukkitAdapter.adapt(e.getLocation()
                .toVector()), tw.getWorld(), 0, 500, location -> {
                    if(location != null)
                        signal.setTargetLocation(BukkitAdapter.adapt(location).toLocation(e.getLocation().getWorld()));
                    main.getDebugLogger().info("Location: " + location);
                }, main);
                finder.run(); // Do this synchronously so eye doesn't change direction several ticks after spawning.
            } else
                main.logger().warning("No overrides are defined for Strongholds. Ender Signals will not work correctly.");
        }
         */
    }
    
    @EventHandler
    public void onCartographerChange(VillagerAcquireTradeEvent e) {
        if(!(e.getEntity() instanceof Villager)) return;
        if(((Villager) e.getEntity()).getProfession().equals(Villager.Profession.CARTOGRAPHER)) {
            platform.logger().severe("Prevented server crash by stopping Cartographer villager from spawning.");
            platform.logger().severe("Please upgrade to Paper, which has a StructureLocateEvent that fixes this issue");
            platform.logger().severe("at the source, and doesn't require us to do stupid band-aids.");
            e.setCancelled(true); // Cancel leveling if the villager is a Cartographer, to prevent crashing server.
        }
    }
    
    @EventHandler
    public void onCartographerLevel(VillagerCareerChangeEvent e) {
        if(e.getProfession().equals(Villager.Profession.CARTOGRAPHER)) {
            platform.logger().severe("Prevented server crash by stopping Cartographer villager from spawning.");
            platform.logger().severe("Please upgrade to Paper, which has a StructureLocateEvent that fixes this issue");
            platform.logger().severe("at the source, and doesn't require us to do stupid band-aids.");
            e.getEntity().setProfession(Villager.Profession.NITWIT); // Give villager new profession to prevent server crash.
            e.setCancelled(true);
        }
    }
}
