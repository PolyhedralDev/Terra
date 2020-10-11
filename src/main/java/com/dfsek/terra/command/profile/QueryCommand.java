package com.dfsek.terra.command.profile;

import com.dfsek.terra.TerraProfiler;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.command.WorldCommand;
import org.polydev.gaea.profiler.WorldProfiler;

import java.util.Collections;
import java.util.List;

public class QueryCommand extends WorldCommand {
    public QueryCommand(org.polydev.gaea.command.Command parent) {
        super(parent);
    }

    @Override
    public boolean execute(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World w) {
        WorldProfiler profile = TerraProfiler.fromWorld(w);
        sender.sendMessage(profile.getResultsFormatted());
        return true;
    }

    @Override
    public String getName() {
        return "query";
    }

    @Override
    public List<org.polydev.gaea.command.Command> getSubCommands() {
        return Collections.emptyList();
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
