package com.dfsek.terra.bukkit.command.command.geometry;

import com.dfsek.terra.api.math.vector.Vector3;
import com.dfsek.terra.api.math.voxel.Tube;
import com.dfsek.terra.bukkit.BukkitCommandSender;
import com.dfsek.terra.bukkit.command.PlayerCommand;
import com.dfsek.terra.bukkit.structure.WorldEditUtil;
import com.dfsek.terra.bukkit.util.BukkitConversions;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class TubeCommand extends PlayerCommand {
    public TubeCommand(com.dfsek.terra.bukkit.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Location[] l = WorldEditUtil.getSelectionPositions(sender);
        if(l == null) return true;
        int radius;
        try {
            radius = Integer.parseInt(args[0]);
        } catch(NumberFormatException e) {
            LangUtil.send("command.geometry.tube.invalid-radius", new BukkitCommandSender(sender), args[0]);
            return true;
        }
        Tube tube = new Tube(BukkitConversions.toTerraVector(l[0].toVector()), BukkitConversions.toTerraVector(l[1].toVector()), radius);
        for(Vector3 v : tube.getGeometry()) {
            v.toLocation(BukkitAdapter.adapt(sender.getWorld())).getBlock().setBlockData(getMain().getWorldHandle().createBlockData("minecraft:stone"), false);
        }
        return true;
    }

    @Override
    public String getName() {
        return "tube";
    }

    @Override
    public List<com.dfsek.terra.bukkit.command.Command> getSubCommands() {
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
