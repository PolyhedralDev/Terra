package com.dfsek.terra.api.lang;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.dfsek.terra.api.entity.CommandSender;


public interface Language {
    void log(String messageID, Level level, Logger logger, String... args);
    
    void send(String messageID, CommandSender sender, String... args);
    
    @SuppressWarnings("unchecked")
    Message getMessage(String id);
}
