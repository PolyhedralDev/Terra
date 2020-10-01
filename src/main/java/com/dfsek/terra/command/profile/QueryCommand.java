package com.dfsek.terra.command.profile;

import com.dfsek.terra.TerraProfiler;
import com.dfsek.terra.command.PlayerCommand;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.profiler.WorldProfiler;

import java.util.Collections;
import java.util.List;

public class QueryCommand extends PlayerCommand {
    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        WorldProfiler profile = TerraProfiler.fromWorld(sender.getWorld());
        sender.sendMessage(profile.getResultsFormatted());
        return true;
    }

    @Override
    public String getName() {
        return "query";
    }

    @Override
    public List<com.dfsek.terra.command.Command> getSubCommands() {
        return Collections.emptyList();
    }
}
