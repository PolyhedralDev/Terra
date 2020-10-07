package com.dfsek.terra.config.lang;

import org.bukkit.command.CommandSender;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SingleLineMessage implements Message {
    private final String message;
    public SingleLineMessage(String message) {
        this.message = message;
    }
    @Override
    public void log(Logger logger, Level level) {
        logger.log(level, message);
    }

    @Override
    public void send(CommandSender sender) {
        sender.sendMessage(message);
    }

    @Override
    public boolean isEmpty() {
        return message == null || message.equals("");
    }
}
