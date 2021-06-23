package com.dfsek.terra.config.lang;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.platform.CommandSender;
import com.dfsek.terra.api.util.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.jar.JarFile;

import static com.dfsek.terra.api.util.JarUtil.copyResourcesToDirectory;

public final class LangUtil {
    private static Language language;

    public static void load(String langID, TerraPlugin main) {
        Logger logger = main.logger();
        File file = new File(main.getDataFolder(), "lang");
        try(JarFile jar = main.getModJar()) {
            copyResourcesToDirectory(jar, "lang", file.toString());
        } catch(IOException | URISyntaxException e) {
            main.getDebugLogger().error("Failed to dump language files!");
            main.getDebugLogger().stack(e);
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

    public static void send(String messageID, CommandSender sender, String... args) {
        language.getMessage(messageID).send(sender, args);
    }
}
