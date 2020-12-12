package com.dfsek.terra.api.gaea.lang;

import com.dfsek.terra.api.generic.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiLineMessage implements Message {
    private final List<String> message;
    public MultiLineMessage(List<String> message) {
        this.message = message;
    }
    @Override
    public void log(Logger logger, Level level, String... args) {
        for(String line: message) {
            logger.log(level, String.format(line, Arrays.asList(args).toArray()));
        }
    }

    @Override
    public void send(CommandSender sender, String... args) {
        for(String line: message) {
            sender.sendMessage(String.format(line, Arrays.asList(args).toArray()));
        }
    }

    @Override
    public boolean isEmpty() {
        return message == null || message.isEmpty();
    }
}
