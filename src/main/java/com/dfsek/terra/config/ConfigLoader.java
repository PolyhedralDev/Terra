package com.dfsek.terra.config;

import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.lang.LangUtil;
import org.apache.commons.io.FilenameUtils;

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
import java.util.logging.Level;
import java.util.stream.Stream;

public class ConfigLoader {
    public static <T extends TerraConfig> Map<String, T> load(Path file, ConfigPack config, Class<T> clazz) {
        long l = System.nanoTime();
        Map<String, T> configs = new HashMap<>();
        //noinspection ResultOfMethodCallIgnored
        file.toFile().mkdirs();
        List<String> ids = new ArrayList<>();
        try(Stream<Path> paths = Files.walk(file)) {
            paths.filter(path -> FilenameUtils.wildcardMatch(path.toFile().getName(), "*.yml"))
                    .forEach(path -> {
                        try {
                            Constructor<T> c = clazz.getConstructor(File.class, ConfigPack.class);
                            T o = c.newInstance(path.toFile(), config);
                            if(ids.contains(o.getID()))
                                LangUtil.log("config.error.duplicate", Level.SEVERE, path.toString());
                            ids.add(o.getID());
                            configs.put(o.getID(), o);
                            LangUtil.log("config.loaded", Level.INFO, o.toString(), path.toString());
                        } catch(IllegalAccessException | InstantiationException | NoSuchMethodException e) {
                            e.printStackTrace();
                            LangUtil.log("config.error.generic", Level.SEVERE, path.toString());
                        } catch(IllegalArgumentException | InvocationTargetException e) {
                            if(ConfigUtil.debug) e.printStackTrace();
                            LangUtil.log("config.error.file", Level.SEVERE, path.toString(), e.getMessage());
                        }
                    });
            LangUtil.log("config.loaded-all", Level.INFO, String.valueOf(configs.size()), clazz.getSimpleName(), String.valueOf((System.nanoTime() - l) / 1000000D));
        } catch(IOException e) {
            e.printStackTrace();
        }
        return configs;
    }
}
