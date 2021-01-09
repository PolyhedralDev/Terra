package com.dfsek.terra.config.files;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.terra.api.util.GlueList;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Loader {
    protected final Map<String, InputStream> streams = new HashMap<>();

    /**
     * Do something with the InputStreams.
     *
     * @param consumer Something to do with the streams.
     */
    public Loader then(ExceptionalConsumer<List<InputStream>> consumer) throws ConfigException {
        consumer.accept(new GlueList<>(streams.values()));
        return this;
    }

    public Loader thenNames(ExceptionalConsumer<List<String>> consumer) throws ConfigException {
        consumer.accept(new GlueList<>(streams.keySet()));
        return this;
    }

    public Loader thenEntries(ExceptionalConsumer<Set<Map.Entry<String, InputStream>>> consumer) throws ConfigException {
        consumer.accept(streams.entrySet());
        return this;
    }

    /**
     * Get a single file from this Loader.
     *
     * @param singleFile File to get
     * @return InputStream from file.
     */
    public abstract InputStream get(String singleFile) throws IOException;

    /**
     * Open a subdirectory.
     *
     * @param directory Directory to open
     * @param extension
     */
    public Loader open(String directory, String extension) {
        if(streams.size() != 0) throw new IllegalStateException("Attempted to load new directory before closing existing InputStreams");
        load(directory, extension);
        return this;
    }

    protected abstract void load(String directory, String extension);

    /**
     * Close all InputStreams opened.
     */
    public Loader close() {
        streams.forEach((name, input) -> {
            try {
                input.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        });
        streams.clear();
        return this;
    }
}
