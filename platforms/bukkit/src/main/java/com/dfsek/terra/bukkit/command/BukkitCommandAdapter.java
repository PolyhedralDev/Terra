package com.dfsek.terra.bukkit.command;

import com.dfsek.terra.api.command.CommandManager;
import com.dfsek.terra.api.command.exception.CommandException;
import com.dfsek.terra.bukkit.world.BukkitAdapter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BukkitCommandAdapter implements CommandExecutor {
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
}
