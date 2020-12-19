package com.dfsek.terra.config.lang;

import com.dfsek.terra.api.lang.Language;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.platform.TerraPlugin;
import com.dfsek.terra.debug.Debug;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dfsek.terra.api.util.JarUtil.copyResourcesToDirectory;

public final class LangUtil {
    private static Language language;
    private static Logger logger;

    public static void load(String langID, TerraPlugin main) {
        logger = main.getLogger();
        File file = new File(main.getDataFolder(), "lang");
        try(JarFile jar = new JarFile(new File(TerraPlugin.class.getProtectionDomain().getCodeSource().getLocation().toURI()))) {
            copyResourcesToDirectory(jar, "lang", file.toString());
        } catch(IOException | URISyntaxException e) {
            Debug.error("Failed to dump language files!");
            e.printStackTrace();
            Debug.error("Report this to Terra!");
        }
        try {
            File file1 = new File(file, langID + ".yml");
            logger.info(file1.getAbsolutePath());
            language = new Language(file1);
            logger.info("Loaded language " + langID);
        } catch(IOException e) {
            logger.severe("Unable to load language: " + langID);
            e.printStackTrace();
            logger.severe("Double-check your configuration before reporting this to Terra!");
        }
    }

    public static Language getLanguage() {
        return language;
    }

    public static void log(String messageID, Level level, String... args) {
        language.getMessage(messageID).log(logger, level, args);
    }

    public static void send(String messageID, CommandSender sender, String... args) {
        language.getMessage(messageID).send(sender, args);
    }
}
