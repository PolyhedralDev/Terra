package com.dfsek.terra.config;

import com.dfsek.terra.config.base.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.polydev.gaea.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ConfigLoader {
    public static  <T extends TerraConfigObject> Map<String, T> load(JavaPlugin main, Path file, TerraConfig config,  Class<T> clazz) {
        Map<String, T> configs = new HashMap<>();
        file.toFile().mkdirs();
        List<String> ids = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(file)) {
            paths
                    .filter(path -> FilenameUtils.wildcardMatch(path.toFile().getName(), "*.yml"))
                    .forEach(path -> {
                        try {
                            Constructor<T> c = clazz.getConstructor(File.class, TerraConfig.class);
                            T o = c.newInstance(path.toFile(), config);
                            if(ids.contains(o.getID())) Bukkit.getLogger().severe("Duplicate ID found in file: " + path.toString());
                            ids.add(o.getID());
                            configs.put(o.getID(), o);
                            main.getLogger().info("Loaded " + o.toString() + " from file " + path.toString());
                        } catch(IllegalAccessException | InstantiationException | NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch(IllegalArgumentException | InvocationTargetException e) {
                            if(ConfigUtil.debug) e.printStackTrace();
                            main.getLogger().severe("Configuration error for Terra object. File: " + path.toString());
                            main.getLogger().severe(((e instanceof InvocationTargetException) ? "INVOCATION: " + e.getCause().getMessage() : e.getMessage()));
                            main.getLogger().severe("Correct this before proceeding!");
                        }
                    });
        } catch(IOException e) {
            e.printStackTrace();
        }
        return configs;
    }
}
