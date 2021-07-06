package com.dfsek.terra.api.lang;

import com.dfsek.terra.api.entity.CommandSender;

import java.util.logging.Level;
import java.util.logging.Logger;

public interface Language {
    @SuppressWarnings("unchecked")
    Message getMessage(String id);

    void log(String messageID, Level level, Logger logger, String... args);

    void send(String messageID, CommandSender sender, String... args);
}
