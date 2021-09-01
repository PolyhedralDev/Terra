package com.dfsek.terra.config.lang;


import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.lang.Message;


public class SingleLineMessage implements Message {
    private final String message;
    
    public SingleLineMessage(String message) {
        this.message = message;
    }
    
    @Override
    public void log(Logger logger, Level level, String... args) {
        logger.log(level, String.format(message, Arrays.asList(args).toArray()));
    }
    
    @Override
    public void send(CommandSender sender, String... args) {
        sender.sendMessage(String.format(message, Arrays.asList(args).toArray()));
    }
    
    @Override
    public boolean isEmpty() {
        return message == null || message.equals("");
    }
}
