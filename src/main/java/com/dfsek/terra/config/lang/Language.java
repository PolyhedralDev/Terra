package com.dfsek.terra.config.lang;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Language extends YamlConfiguration {
    public Language(File file) throws IOException, InvalidConfigurationException {
        load(file);
    }
    @Override
    public void load(@NotNull File file) throws IOException, InvalidConfigurationException {
        super.load(file);
    }
    public Message getMessage(String id) {
        Object m = get(id);
        Message temp;
        if(m instanceof List) {
            temp = new MultiLineMessage(getStringList(id));
        } else if(m instanceof String) {
            temp = new SingleLineMessage(getString(id));
        } else return new SingleLineMessage("message:" + id + ":translation_undefined");
        if(temp.isEmpty()) return new SingleLineMessage("message:" + id + ":translation_undefined");
        return temp;
    }
}
