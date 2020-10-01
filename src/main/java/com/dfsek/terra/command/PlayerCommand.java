package com.dfsek.terra.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerCommand extends Command {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Command is for players only.");
            return true;
        }
        Player p = (Player) sender;
        return onCommand(p, command, label, args);
    }
    public abstract boolean onCommand(@NotNull Player sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args);
}
