package com.dfsek.terra.config.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Load all {@code *.yml} files from a {@link java.nio.file.Path}.
 */
public class FolderLoader extends Loader {
    private final Path path;

    public FolderLoader(Path path) {
        this.path = path;
    }

    @Override
    public Loader open(String directory) {
        if(streams.size() != 0) throw new IllegalStateException("Attempted to load folder before closing InputStreams");
        File newPath = new File(path.toFile(), directory);
        newPath.mkdirs();
        try(Stream<Path> paths = Files.walk(newPath.toPath())) {
            paths.filter(Files::isRegularFile).filter(file -> file.toString().toLowerCase().endsWith(".yml")).forEach(file -> {
                try {
                    streams.add(new FileInputStream(file.toFile()));
                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
        } catch(IOException e) {
            e.printStackTrace();
        }
        return this;
    }
}
