package com.dfsek.terra.bukkit.listeners;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.world.locate.AsyncStructureFinder;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.world.TerraWorld;
import com.dfsek.terra.world.population.items.TerraStructure;
import io.papermc.paper.event.world.StructureLocateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PaperListener implements Listener {
    private final TerraPlugin main;

    public PaperListener(TerraPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onStructureLocate(StructureLocateEvent e) {
        if(!TerraWorld.isTerraWorld(BukkitAdapter.adapt(e.getWorld()))) return;
        e.setResult(null); // Assume no result.
        String name = "minecraft:" + e.getType().getName();
        main.getDebugLogger().info("Overriding structure location for \"" + name + "\"");
        TerraWorld tw = main.getWorld(BukkitAdapter.adapt(e.getWorld()));
        TerraStructure config = tw.getConfig().getRegistry(TerraStructure.class).get(tw.getConfig().getTemplate().getLocatable().get(name));
        if(config != null) {
            AsyncStructureFinder finder = new AsyncStructureFinder(tw.getBiomeProvider(), config, BukkitAdapter.adapt(e.getOrigin()), 0, 500, location -> {
                if(location != null)
                    e.setResult(BukkitAdapter.adapt(location.toLocation(BukkitAdapter.adapt(e.getWorld()))));
                main.getDebugLogger().info("Location: " + location);
            }, main);
            finder.run(); // Do this synchronously.
        } else {
            main.logger().warning("No overrides are defined for \"" + name + "\"");
        }

    }


}
