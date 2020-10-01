package com.dfsek.terra.command.type;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public abstract class Command {
    public abstract String getName();
    public abstract List<Command> getSubCommands();
    public abstract boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args);
    public abstract int arguments();
    public final boolean execute(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length > 0) {
            for(Command c : getSubCommands()) {
                if(c.getName().equals(args[0])) return c.execute(sender, command, label, Arrays.stream(args, 1, args.length).toArray(String[]::new));
            }
            if(args.length != arguments()) {
                sender.sendMessage("Invalid command.");
                return true;
            }
            return onCommand(sender, command, label, args);
        }
        return onCommand(sender, command, label, new String[] {});
    }

}
