package com.dfsek.terra.config.lang;

import com.dfsek.terra.Terra;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public final class LangUtil {
    private static Language language;
    private static Logger logger;
    public static void load(String langID, JavaPlugin main) {
        logger = main.getLogger();
        try {
            language = new Language(new File(Terra.getInstance().getDataFolder(), "lang" + File.separator + langID + ".yml"));
        } catch(InvalidConfigurationException | IOException e) {
            logger.severe("Unable to load language: " + langID);
            e.printStackTrace();
            logger.severe("Double-check your configuration before reporting this to Terra!");
        }
    }
}
