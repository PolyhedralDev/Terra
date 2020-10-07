package com.dfsek.terra.command.type;

import com.dfsek.terra.Debug;
import com.dfsek.terra.config.lang.LangUtil;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a command or subcommand, can be nested via getSubCommands.
 */
public abstract class Command implements CommandExecutor, TabCompleter {
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

    public abstract boolean execute(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args);
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
    @Override
    public final boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length > 0) {
            for(Command c : getSubCommands()) {
                if(c.getName().equals(args[0])) return c.onCommand(sender, command, label, Arrays.stream(args, 1, args.length).toArray(String[]::new));
            }
            if(args.length != arguments()) {
                LangUtil.send("command.invalid", sender, String.valueOf(arguments()), String.valueOf(args.length));
                return true;
            }
            return execute(sender, command, label, args);
        }
        if(args.length != arguments()) {
            LangUtil.send("command.invalid", sender, String.valueOf(arguments()), String.valueOf(args.length));
            return true;
        }
        return execute(sender, command, label, new String[] {});
    }

    public abstract List<String> getTabCompletions(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args);

    @Override
    public final @Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> complete = new ArrayList<>();
        if(args.length > 0) for(Command c : getSubCommands()) {
            if(c.getName().startsWith(args[0])) complete.add(c.getName());
            if(c.getName().equals(args[0])) return c.onTabComplete(sender, command, alias, Arrays.stream(args, 1, args.length).toArray(String[]::new));
        }
        complete.addAll(getTabCompletions(sender, alias, args));
        return complete;
    }
}
