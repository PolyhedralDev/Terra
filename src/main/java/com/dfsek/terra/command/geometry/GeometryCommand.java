package com.dfsek.terra.command.geometry;

import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.DebugCommand;
import org.polydev.gaea.command.PlayerCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GeometryCommand extends PlayerCommand implements DebugCommand {
    public GeometryCommand(org.polydev.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        LangUtil.send("command.geometry.main-menu", sender);
        return true;
    }

    @Override
    public String getName() {
        return "geometry";
    }

    @Override
    public List<org.polydev.gaea.command.Command> getSubCommands() {
        return Arrays.asList(new SphereCommand(this), new TubeCommand(this), new DeformedSphereCommand(this));
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
