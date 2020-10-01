package com.dfsek.terra.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public abstract class Command {
    public abstract String getName();
    public abstract List<Command> getSubCommands();
    public abstract boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args);
    public final boolean execute(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length > 0) {
            for(com.dfsek.terra.command.Command c : getSubCommands()) {
                if(c.getName().equals(args[0])) return c.execute(sender, command, label, Arrays.stream(args, 1, args.length).toArray(String[]::new));
            }
            sender.sendMessage("Invalid command.");
            return true;
        } else {
            onCommand(sender, command, label, args);
        }
        return true;
    }
}
