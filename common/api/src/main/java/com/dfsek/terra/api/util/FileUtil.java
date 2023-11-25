package com.dfsek.terra.api.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Function.*;


public class FileUtil {
    public static Map<String, Path> filesWithExtension(Path start, String... extensions) throws IOException {
        if(Files.notExists(start) || !Files.isDirectory(start)) return Collections.emptyMap();
        try (Stream<Path> paths = Files.walk(start)) {
            return paths
                .filter(Files::isRegularFile)
                .filter(p -> Arrays.stream(extensions).anyMatch(e -> p.getFileName().toString().endsWith(e)))
                .collect(Collectors.toMap(p -> start.relativize(p).toString(), identity()));
        }
    }

    public static String fileName(String path) {
        if(path.contains(File.separator)) {
            return path.substring(path.lastIndexOf(File.separatorChar) + 1, path.lastIndexOf('.'));
        } else if(path.contains("/")) {
            return path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf('.'));
        } else if(path.contains(".")) {
            return path.substring(0, path.lastIndexOf('.'));
        } else {
            return path;
        }
    }
}
