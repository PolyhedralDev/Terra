package com.dfsek.terra.bukkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.dfsek.terra.api.command.CommandManager;
import com.dfsek.terra.api.command.exception.CommandException;
import com.dfsek.terra.bukkit.world.BukkitAdapter;


public class BukkitCommandAdapter implements CommandExecutor, TabCompleter {
    private static final Logger logger = LoggerFactory.getLogger(BukkitCommandAdapter.class);
    
    private final CommandManager manager;
    
    public BukkitCommandAdapter(CommandManager manager) {
        this.manager = manager;
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> argList = new ArrayList<>(Arrays.asList(args));
        if(argList.isEmpty()) {
            sender.sendMessage("Command requires arguments.");
            return true;
        }
        try {
            manager.execute(argList.remove(0), BukkitAdapter.adapt(sender), argList);
        } catch(CommandException e) {
            sender.sendMessage(e.getMessage());
        }
        return true;
    }
    
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
                                                @NotNull String[] args) {
        List<String> argList = new ArrayList<>(Arrays.asList(args));
        
        try {
            return manager.tabComplete(argList.remove(0), BukkitAdapter.adapt(sender), argList).stream()
                          .filter(s -> s.toLowerCase(Locale.ROOT).startsWith(args[args.length - 1].toLowerCase(Locale.ROOT))).sorted(
                            String::compareTo).collect(Collectors.toList());
        } catch(CommandException e) {
            logger.warn("Exception occurred during tab completion", e);
            return Collections.emptyList();
        }
    }
}
