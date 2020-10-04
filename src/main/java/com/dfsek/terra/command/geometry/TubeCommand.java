package com.dfsek.terra.command.geometry;

import com.dfsek.terra.WorldEditUtil;
import com.dfsek.terra.command.type.PlayerCommand;
import com.dfsek.terra.procgen.voxel.Tube;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class TubeCommand extends PlayerCommand {
    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Location[] l = WorldEditUtil.getSelectionPositions(sender);
        if(l == null) return true;
        int radius;
        try {
            radius = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            sender.sendMessage("Invalid radius: " + args[0]);
            return true;
        }
        Tube tube = new Tube(l[0].toVector(), l[1].toVector(), radius);
        for(Vector v : tube.getGeometry()) {
            v.toLocation(sender.getWorld()).getBlock().setType(Material.STONE);
        }
        return true;
    }

    @Override
    public String getName() {
        return "tube";
    }

    @Override
    public List<com.dfsek.terra.command.type.Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public int arguments() {
        return 1;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
