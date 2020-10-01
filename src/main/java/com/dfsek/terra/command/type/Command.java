package com.dfsek.terra.command.type;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a command or subcommand, can be nested via getSubCommands.
 */
public abstract class Command {
    /**
     * Gets the name of the command/subcommand
     * @return Name of command
     */
    public abstract String getName();

    /**
     * Gets a list of subcommands
     * @return List of subcommands
     */
    public abstract List<Command> getSubCommands();

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if a valid command, otherwise false
     */
    public abstract boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args);

    /**
     * Gets the number of arguments
     * @return Number of arguments
     */
    public abstract int arguments();

    /**
     * Executes the given command, invoking subcommands if applicable and returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if a valid command, otherwise false
     */
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
