package com.dfsek.terra.config.lang;


import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.yaml.YamlConfiguration;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.lang.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LanguageImpl implements com.dfsek.terra.api.lang.Language {
    private final Configuration configuration;

    public LanguageImpl(File file) throws IOException {
        configuration = new YamlConfiguration(new FileInputStream(file), file.getName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Message getMessage(String id) {
        Message temp = null;
        if(configuration.contains(id)) {
            Object m = configuration.get(id);

            if(m instanceof List) {
                temp = new MultiLineMessage((List<String>) m);
            } else if(m instanceof String) {
                temp = new SingleLineMessage((String) m);
            } else return new SingleLineMessage("message:" + id + ":translation_undefined");
        }
        if(temp == null || temp.isEmpty()) return new SingleLineMessage("message:" + id + ":translation_undefined");
        return temp;
    }

    @Override
    public void log(String messageID, Level level, Logger logger, String... args) {
        getMessage(messageID).log(logger, level, args);
    }

    @Override
    public void send(String messageID, CommandSender sender, String... args) {
        getMessage(messageID).send(sender, args);
    }
}
