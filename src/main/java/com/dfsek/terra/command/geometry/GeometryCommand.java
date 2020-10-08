package com.dfsek.terra.command.geometry;

import com.dfsek.terra.command.type.DebugCommand;
import com.dfsek.terra.command.type.PlayerCommand;
import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GeometryCommand extends PlayerCommand implements DebugCommand {
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
