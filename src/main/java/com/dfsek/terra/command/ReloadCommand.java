package com.dfsek.terra.command;

import com.dfsek.terra.Terra;
import com.dfsek.terra.command.type.Command;
import com.dfsek.terra.config.ConfigUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ReloadCommand extends Command {
    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public List<Command> getSubCommands() {
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ConfigUtil.loadConfig(Terra.getInstance());
        sender.sendMessage("Reloaded Terra config.");
        return true;
    }

    @Override
    public int arguments() {
        return 0;
    }
}
