package com.dfsek.terra.config.files;

import com.dfsek.tectonic.exception.ConfigException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Load all {@code *.yml} files from a {@link java.nio.file.Path}.
 */
public class FolderLoader implements Loader {
    private final List<InputStream> streams = new ArrayList<>();

    public FolderLoader(Path path) {
        path.toFile().mkdirs();
        try(Stream<Path> paths = Files.walk(path)) {
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
    }

    public FolderLoader then(ExceptionalConsumer<List<InputStream>> consumer) throws ConfigException {
        consumer.accept(streams);
        return this;
    }

    @Override
    public void close() {
        streams.forEach(stream -> {
            try {
                stream.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
        streams.clear();
    }
}
