package com.dfsek.terra.bukkit.listeners;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.structure.configured.ConfiguredStructure;
import com.dfsek.terra.api.world.TerraWorld;
import com.dfsek.terra.api.world.World;
import com.dfsek.terra.api.world.locate.AsyncStructureFinder;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
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
        if(!BukkitAdapter.adapt(e.getWorld()).isTerraWorld()) return;
        String name = "minecraft:" + e.getType().getName();
        main.getDebugLogger().info("Overriding structure location for \"" + name + "\"");
        World w = BukkitAdapter.adapt(e.getWorld());
        ConfiguredStructure config = tw.getConfig().getRegistry(TerraStructure.class).get(tw.getConfig().getLocatable().get(name));
        if(config != null) {
            AsyncStructureFinder finder = new AsyncStructureFinder(w.getBiomeProvider(), config, BukkitAdapter.adapt(e.getOrigin().toVector()), tw.getWorld(), 0, 500, location -> {
                if(location != null)
                    e.setResult(BukkitAdapter.adapt(location).toLocation(e.getWorld()));
                main.getDebugLogger().info("Location: " + location);
            }, main);
            finder.run(); // Do this synchronously.
        } else {
            e.setResult(e.getOrigin());
            main.logger().warning("No overrides are defined for \"" + name + "\". Locating this structure will NOT work properly!");
        }
    }
}
