package com.dfsek.terra.api.language;


import com.dfsek.terra.api.platform.CommandSender;

import java.util.logging.Level;
import java.util.logging.Logger;

public interface Message {
    void log(Logger logger, Level level, String... args);
    void send(CommandSender sender, String... args);
    boolean isEmpty();
}
