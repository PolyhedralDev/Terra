package com.dfsek.terra.config.lang;

import com.dfsek.terra.api.bukkit.TerraBukkitPlugin;
import com.dfsek.terra.api.gaea.lang.Language;
import com.dfsek.terra.debug.Debug;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.dfsek.terra.api.gaea.util.JarUtil.copyResourcesToDirectory;

public final class LangUtil {
    private static Language language;
    private static Logger logger;

    public static void load(String langID, JavaPlugin main) {
        logger = main.getLogger();
        File file = new File(main.getDataFolder(), "lang");
        try(JarFile jar = new JarFile(new File(TerraBukkitPlugin.class.getProtectionDomain().getCodeSource().getLocation().toURI()))) {
            copyResourcesToDirectory(jar, "lang", file.toString());
        } catch(IOException | URISyntaxException e) {
            Debug.error("Failed to dump language files!");
            e.printStackTrace();
            Debug.error("Report this to Terra!");
        }
        try {
            language = new Language(new File(main.getDataFolder(), "lang" + File.separator + langID + ".yml"));
        } catch(InvalidConfigurationException | IOException e) {
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
