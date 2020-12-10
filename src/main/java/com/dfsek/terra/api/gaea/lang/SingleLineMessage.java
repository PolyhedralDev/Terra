package com.dfsek.terra.api.gaea.lang;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SingleLineMessage implements Message {
    private final String message;
    public SingleLineMessage(String message) {
        this.message = message;
    }
    @Override
    public void log(Logger logger, Level level, String... args) {
        logger.log(level, ChatColor.translateAlternateColorCodes('&', String.format(message, Arrays.asList(args).toArray())));
    }

    @Override
    public void send(CommandSender sender, String... args) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(message, Arrays.asList(args).toArray())));
    }

    @Override
    public boolean isEmpty() {
        return message == null || message.equals("");
    }
}
