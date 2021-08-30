package com.dfsek.terra.bukkit.structure;

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
import org.jetbrains.annotations.NotNull;


public final class WorldEditUtil {
    public static Location[] getSelectionLocations(Player sender) {
        WorldEditPlugin we;
        try {
            we = WorldEditUtil.getWorldEdit();
        } catch(WorldEditNotFoundException e) {
            sender.sendMessage("WorldEdit is not installed! Please install WorldEdit before attempting to export structures.");
            throw e;
        }
        Region selection;
        try {
            selection = we.getSession(sender).getSelection(BukkitAdapter.adapt(sender.getWorld()));
        } catch(IncompleteRegionException | ClassCastException e) {
            throw new IllegalStateException("Invalid/incomplete selection!");
        }
        if(selection == null) {
            throw new IllegalStateException("Please make a selection before attempting to export!");
        }
        BlockVector3 min = selection.getMinimumPoint();
        BlockVector3 max = selection.getMaximumPoint();
        Location l1 = new Location(sender.getWorld(), min.getBlockX(), min.getBlockY(), min.getBlockZ());
        Location l2 = new Location(sender.getWorld(), max.getBlockX(), max.getBlockY(), max.getBlockZ());
        return new Location[]{ l1, l2 };
    }
    
    /**
     * Gets an instance of the WorldEditPlugin class.
     *
     * @return The world edit plugin instance.
     *
     * @throws WorldEditNotFoundException Thrown when worldedit cannot be found.
     */
    @NotNull
    public static WorldEditPlugin getWorldEdit() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if(p instanceof WorldEditPlugin) return (WorldEditPlugin) p;
        Bukkit.getLogger().severe("[Terra] a command requiring WorldEdit was executed, but WorldEdit was not detected!");
        throw new WorldEditNotFoundException("Could not find World Edit!");
    }
    
    public static Location[] getSelectionPositions(Player sender) {
        WorldEditPlugin we;
        try {
            we = WorldEditUtil.getWorldEdit();
        } catch(WorldEditNotFoundException e) {
            sender.sendMessage("WorldEdit is not installed! Please install WorldEdit before attempting to export structures.");
            throw e;
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
        return new Location[]{ l1, l2 };
    }
}
