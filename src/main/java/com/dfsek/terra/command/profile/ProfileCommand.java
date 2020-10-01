package com.dfsek.terra.command.profile;

import com.dfsek.terra.command.type.WorldCommand;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class ProfileCommand extends WorldCommand {
    @Override
    public boolean onCommand(@NotNull Player sender, @NotNull Command command, @NotNull String label, @NotNull String[] args, World w) {
        sender.sendMessage("---------------Terra/profile---------------");
        sender.sendMessage("start - Starts the profiler");
        sender.sendMessage("stop  - Stops the profiler");
        sender.sendMessage("query - Fetches profiler data");
        sender.sendMessage("reset - Resets profiler data");
        return true;
    }

    @Override
    public String getName() {
        return "profile";
    }

    @Override
    public List<com.dfsek.terra.command.type.Command> getSubCommands() {
        return Arrays.asList(new QueryCommand(), new ResetCommand(), new StartCommand(), new StopCommand());
    }

    @Override
    public int arguments() {
        return 1;
    }
}
