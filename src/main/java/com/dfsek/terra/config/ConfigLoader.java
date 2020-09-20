package com.dfsek.terra.config;

import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ConfigLoader {
    private final String path;
    public ConfigLoader(String path) {
        this.path = path;
    }

    public <T extends TerraConfigObject> void load(JavaPlugin main, Class<T> clazz) {
        File folder = new File(main.getDataFolder() + File.separator + path);
        folder.mkdirs();
        try (Stream<Path> paths = Files.walk(folder.toPath())) {
            paths
                    .filter(path -> FilenameUtils.wildcardMatch(path.toFile().getName(), "*.yml"))
                    .forEach(path -> {
                        try {
                            Constructor<T> c = clazz.getConstructor(File.class);
                            T o = c.newInstance(path.toFile());
                            main.getLogger().info("Loaded " + o.toString() + " from file " + path.toString());
                        } catch(IllegalAccessException | InstantiationException | NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch(IllegalArgumentException | InvocationTargetException e) {
                            e.printStackTrace();
                            main.getLogger().severe("Configuration error for Terra object. File: " + path.toString());
                            main.getLogger().severe(((e instanceof InvocationTargetException) ? "INVOCATION: " + e.getCause().getMessage() : e.getMessage()));
                            main.getLogger().severe("Correct this before proceeding!");
                        }
                    });
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
