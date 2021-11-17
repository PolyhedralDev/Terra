package com.dfsek.terra.bukkit.listeners;

import io.papermc.paper.event.world.StructureLocateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.dfsek.terra.api.Platform;


public class PaperListener implements Listener {
    private final Platform platform;
    
    public PaperListener(Platform platform) {
        this.platform = platform;
    }
    
    @EventHandler
    public void onStructureLocate(StructureLocateEvent e) {
        /*String name = "minecraft:" + e.getType().getName();
        main.getDebugLogger().info("Overriding structure location for \"" + name + "\"");
        World w = BukkitAdapter.adapt(e.getWorld());
        ConfiguredStructure config = tw.getConfig().getRegistry(TerraStructure.class).get(tw.getConfig().getLocatable().get(name));
        if(config != null) {
            AsyncStructureFinder finder = new AsyncStructureFinder(w.getBiomeProvider(), config, BukkitAdapter.adapt(e.getOrigin()
            .toVector()), tw.getWorld(), 0, 500, location -> {
                if(location != null)
                    e.setResult(BukkitAdapter.adapt(location).toLocation(e.getWorld()));
                main.getDebugLogger().info("Location: " + location);
            }, main);
            finder.run(); // Do this synchronously.
        } else {
            e.setResult(e.getOrigin());
            main.logger().warning("No overrides are defined for \"" + name + "\". Locating this structure will NOT work properly!");
        }
         */
    }
}
