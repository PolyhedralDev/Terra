package com.dfsek.terra.config.lang;

import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiLineMessage implements Message {
    private final List<String> message;
    public MultiLineMessage(List<String> message) {
        this.message = message;
    }
    @Override
    public void log(Logger logger, Level level) {
        for(String line: message) {
            logger.log(level, line);
        }
    }

    @Override
    public void send(CommandSender sender) {
        for(String line: message) {
            sender.sendMessage(line);
        }
    }

    @Override
    public boolean isEmpty() {
        return message == null || message.isEmpty();
    }
}
