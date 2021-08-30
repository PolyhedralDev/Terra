package com.dfsek.terra.config.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.lang.Language;


public final class LangUtil {
    private static final Logger logger = LoggerFactory.getLogger(LangUtil.class);
    
    private static Language language;
    
    public static void load(String langID, TerraPlugin main) {
        File file = new File(main.getDataFolder(), "lang");
        try {
            File file1 = new File(file, langID + ".yml");
            logger.info(file1.getAbsolutePath());
            language = new LanguageImpl(file1);
            logger.info("Loaded language {}", langID);
        } catch(IOException e) {
            logger.error("Unable to load language: {}.\nDouble-check your configuration before reporting this to Terra!", langID, e);
        }
    }
    
    public static Language getLanguage() {
        return language;
    }
    
    public static void send(String messageID, CommandSender sender, String... args) {
        language.getMessage(messageID).send(sender, args);
    }
}
