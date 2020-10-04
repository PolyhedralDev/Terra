package com.dfsek.terra.command.geometry;

import com.dfsek.terra.command.type.PlayerCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GeometryCommand extends PlayerCommand {
    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage("---------------Terra/geometry----------------");
        sender.sendMessage("Various voxel geometry debugging commands");
        sender.sendMessage("sphere       - Generate a sphere");
        sender.sendMessage("deformsphere - Generate a deformed sphere");
        sender.sendMessage("tube         - Generate a tube");
        return true;
    }

    @Override
    public String getName() {
        return "geometry";
    }

    @Override
    public List<com.dfsek.terra.command.type.Command> getSubCommands() {
        return Arrays.asList(new SphereCommand(), new TubeCommand(), new DeformedSphereCommand());
    }

    @Override
    public int arguments() {
        return 0;
    }

    @Override
    public List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
