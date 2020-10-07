package com.dfsek.terra.config.lang;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Language extends YamlConfiguration {
    private final Map<String, Message> messages;
    public Language(File file) throws IOException, InvalidConfigurationException {
        load(file);
        messages = new HashMap<>();
        messages.put("enable", new MultiLineMessage(getStringList("enable")));
        messages.put("disable", new MultiLineMessage(getStringList("disable")));
    }
    @Override
    public void load(@NotNull File file) throws IOException, InvalidConfigurationException {
        super.load(file);
    }
    public Message getMessage(String id) {
        Message temp = messages.get(id);
        if(temp == null || temp.isEmpty()) return new SingleLineMessage(id + ":translation_undefined");
        return temp;
    }
}
