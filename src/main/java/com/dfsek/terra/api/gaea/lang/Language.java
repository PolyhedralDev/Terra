package com.dfsek.terra.api.gaea.lang;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Language extends YamlConfiguration {
    public Language(File file) throws IOException, InvalidConfigurationException {
        load(file);
    }
    @Override
    public void load(@NotNull File file) throws IOException, InvalidConfigurationException {
        super.load(file);
    }
    @SuppressWarnings("unchecked")
    public Message getMessage(String id) {
        Object m = get(id);
        Message temp;
        if(m instanceof List) {
            temp = new MultiLineMessage((List<String>) m);
        } else if(m instanceof String) {
            temp = new SingleLineMessage((String) m);
        } else return new SingleLineMessage("message:" + id + ":translation_undefined");
        if(temp.isEmpty()) return new SingleLineMessage("message:" + id + ":translation_undefined");
        return temp;
    }
    public void log(String messageID, Level level, Logger logger, String... args) {
        getMessage(messageID).log(logger, level, args);
    }
    public void send(String messageID, CommandSender sender, String... args) {
        getMessage(messageID).send(sender, args);
    }
}
