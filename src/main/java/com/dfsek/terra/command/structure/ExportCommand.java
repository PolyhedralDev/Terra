package com.dfsek.terra.command.structure;

import com.dfsek.terra.Terra;
import com.dfsek.terra.WorldEditUtil;
import com.dfsek.terra.command.PlayerCommand;
import com.dfsek.terra.structure.GaeaStructure;
import com.dfsek.terra.structure.InitializationException;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ExportCommand extends PlayerCommand {
    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        WorldEditPlugin we = WorldEditUtil.getWorldEdit();
        if(we == null) {
            sender.sendMessage("WorldEdit is not installed! Please install WorldEdit before attempting to export structures.");
            return true;
        }
        Region selection;
        try {
            selection = we.getSession(sender).getSelection(BukkitAdapter.adapt(sender.getWorld()));
        } catch(IncompleteRegionException e) {
            sender.sendMessage("Invalid/incomplete selection!");
            return true;
        }
        BukkitAdapter.adapt(sender);
        if(selection == null) {
            sender.sendMessage("Please make a selection before attempting to export!");
            return true;
        }
        BlockVector3 min = selection.getMinimumPoint();
        BlockVector3 max = selection.getMaximumPoint();
        Location l1 = new Location(sender.getWorld(), min.getBlockX(), min.getBlockY(), min.getBlockZ());
        Location l2 = new Location(sender.getWorld(), max.getBlockX(), max.getBlockY(), max.getBlockZ());
        GaeaStructure structure = null;
        try {
            structure = new GaeaStructure(l1, l2, args[0]);
        } catch(InitializationException e) {
            sender.sendMessage(e.getMessage());
            return true;
        }
        try {
            File file = new File(Terra.getInstance().getDataFolder() + File.separator + "export" + File.separator + "structures", args[0] + ".tstructure");
            file.getParentFile().mkdirs();
            file.createNewFile();
            structure.save(file);
            sender.sendMessage("Saved structure with ID " + structure.getId() + ", UUID: " + structure.getUuid().toString() + " to " + file.getPath());
        } catch(IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String getName() {
        return "export";
    }

    @Override
    public List<com.dfsek.terra.command.Command> getSubCommands() {
        return Collections.emptyList();
    }
}
