package com.dfsek.terra.api.lang;


import java.util.logging.Level;
import java.util.logging.Logger;

import com.dfsek.terra.api.entity.CommandSender;


public interface Message {
    void log(Logger logger, Level level, String... args);
    
    void send(CommandSender sender, String... args);
    
    boolean isEmpty();
}
