package com.dfsek.terra.config.lang;

import com.dfsek.terra.Debug;
import com.dfsek.terra.Terra;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LangUtil {
    private static Language language;
    private static Logger logger;
    public static void load(String langID, JavaPlugin main) {
        logger = main.getLogger();
        File file = new File(main.getDataFolder(), "lang");
        try(JarFile jar = new JarFile(new File(Terra.class.getProtectionDomain().getCodeSource().getLocation().toURI()))) {
            copyResourcesToDirectory(jar, file.toString());
        } catch(IOException | URISyntaxException e) {
            Debug.error("Failed to dump language files!");
            e.printStackTrace();
            Debug.error("Report this to Terra!");
        }
        try {
            language = new Language(new File(Terra.getInstance().getDataFolder(), "lang" + File.separator + langID + ".yml"));
        } catch(InvalidConfigurationException | IOException e) {
            logger.severe("Unable to load language: " + langID);
            e.printStackTrace();
            logger.severe("Double-check your configuration before reporting this to Terra!");
        }
    }
    private static void copyResourcesToDirectory(JarFile fromJar, String destDir) throws IOException {
        for(Enumeration<JarEntry> entries = fromJar.entries(); entries.hasMoreElements(); ) {
            JarEntry entry = entries.nextElement();
            if(entry.getName().startsWith("lang" + "/") && ! entry.isDirectory()) {
                File dest = new File(destDir + File.separator + entry.getName().substring("lang".length() + 1));
                Debug.info("Output: " + dest.toString());
                if(dest.exists()) continue;
                File parent = dest.getParentFile();
                if(parent != null) {
                    parent.mkdirs();
                }
                Debug.info("Output does not already exist. Creating... ");
                try(FileOutputStream out = new FileOutputStream(dest); InputStream in = fromJar.getInputStream(entry)) {
                    byte[] buffer = new byte[8 * 1024];

                    int s;
                    while((s = in.read(buffer)) > 0) {
                        out.write(buffer, 0, s);
                    }
                } catch(IOException e) {
                    throw new IOException("Could not copy asset from jar file", e);
                }
            }
        }
    }
    public static void log(String messageID, Level level, String... args) {
        language.getMessage(messageID).log(logger, level, args);
    }
    public static void send(String messageID, CommandSender sender, String... args) {
        language.getMessage(messageID).send(sender, args);
    }
}
