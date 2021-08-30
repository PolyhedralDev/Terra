package com.dfsek.terra.bukkit;

import org.bukkit.ChatColor;

import com.dfsek.terra.api.entity.CommandSender;


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
