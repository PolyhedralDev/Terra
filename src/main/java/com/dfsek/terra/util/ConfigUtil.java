package com.dfsek.terra.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class ConfigUtil {
    public static List<InputStream> loadFromPath(Path folder) {
        List<InputStream> streams = new ArrayList<>();
        try(Stream<Path> paths = Files.walk(folder)) {
            paths.filter(Files::isRegularFile).filter(file -> file.endsWith(".yml")).forEach(file -> {
                try {
                    streams.add(new FileInputStream(file.toFile()));
                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
        } catch(IOException e) {
            e.printStackTrace();
        }

        return streams;
    }
}
