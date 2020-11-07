package com.dfsek.terra.util.structure;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class WorldEditUtil {
    public static Location[] getSelectionLocations(Player sender) {
        WorldEditPlugin we = WorldEditUtil.getWorldEdit();
        if(we == null) {
            sender.sendMessage("WorldEdit is not installed! Please install WorldEdit before attempting to export structures.");
            return null;
        }
        Region selection;
        try {
            selection = we.getSession(sender).getSelection(BukkitAdapter.adapt(sender.getWorld()));
        } catch(IncompleteRegionException | ClassCastException e) {
            sender.sendMessage("Invalid/incomplete selection!");
            return null;
        }
        if(selection == null) {
            sender.sendMessage("Please make a selection before attempting to export!");
            return null;
        }
        BlockVector3 min = selection.getMinimumPoint();
        BlockVector3 max = selection.getMaximumPoint();
        Location l1 = new Location(sender.getWorld(), min.getBlockX(), min.getBlockY(), min.getBlockZ());
        Location l2 = new Location(sender.getWorld(), max.getBlockX(), max.getBlockY(), max.getBlockZ());
        return new Location[] {l1, l2};
    }

    public static Location[] getSelectionPositions(Player sender) {
        WorldEditPlugin we = WorldEditUtil.getWorldEdit();
        if(we == null) {
            sender.sendMessage("WorldEdit is not installed! Please install WorldEdit before attempting to export structures.");
            return null;
        }
        CuboidRegion selection;
        try {
            selection = (CuboidRegion) we.getSession(sender).getSelection(BukkitAdapter.adapt(sender.getWorld()));
        } catch(IncompleteRegionException | ClassCastException e) {
            sender.sendMessage("Invalid/incomplete selection!");
            return null;
        }
        if(selection == null) {
            sender.sendMessage("Please make a selection before attempting to export!");
            return null;
        }
        BlockVector3 min = selection.getPos1();
        BlockVector3 max = selection.getPos2();
        Location l1 = new Location(sender.getWorld(), min.getBlockX(), min.getBlockY(), min.getBlockZ());
        Location l2 = new Location(sender.getWorld(), max.getBlockX(), max.getBlockY(), max.getBlockZ());
        return new Location[] {l1, l2};
    }

    public static WorldEditPlugin getWorldEdit() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if(p instanceof WorldEditPlugin) return (WorldEditPlugin) p;
        Bukkit.getLogger().severe("[Terra] a command requiring WorldEdit was executed, but WorldEdit was not detected!");
        return null;
    }
}
