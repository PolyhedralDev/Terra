package com.dfsek.terra.config.fileloaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    public InputStream get(String singleFile) throws IOException {
        return new FileInputStream(new File(path.toFile(), singleFile));
    }

    @Override
    protected void load(String directory, String extension) {
        File newPath = new File(path.toFile(), directory);
        newPath.mkdirs();
        try(Stream<Path> paths = Files.walk(newPath.toPath())) {
            paths.filter(Files::isRegularFile).filter(file -> file.toString().toLowerCase().endsWith(extension)).forEach(file -> {
                try {
                    String rel = newPath.toPath().relativize(file).toString();
                    streams.put(rel, new FileInputStream(file.toFile()));
                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
