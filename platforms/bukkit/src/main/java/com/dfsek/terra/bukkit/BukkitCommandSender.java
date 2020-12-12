package com.dfsek.terra.bukkit;

import com.dfsek.terra.api.generic.CommandSender;
import org.bukkit.ChatColor;

public class BukkitCommandSender implements CommandSender {
    private final org.bukkit.command.CommandSender delegate;

    public BukkitCommandSender(org.bukkit.command.CommandSender delegate) {
        this.delegate = delegate;
    }

    @Override
    public void sendMessage(String message) {
        delegate.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    @Override
    public org.bukkit.command.CommandSender getHandle() {
        return delegate;
    }
}
