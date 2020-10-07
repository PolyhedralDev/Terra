package com.dfsek.terra.config.lang;

import org.bukkit.command.CommandSender;

import java.util.logging.Level;
import java.util.logging.Logger;

public interface Message {
    void log(Logger logger, Level level);
    void send(CommandSender sender);
    boolean isEmpty();
}
