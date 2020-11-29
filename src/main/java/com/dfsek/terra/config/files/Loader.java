package com.dfsek.terra.config.files;

import com.dfsek.tectonic.exception.ConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class Loader {
    protected final List<InputStream> streams = new ArrayList<>();

    /**
     * Do something with the InputStreams.
     *
     * @param consumer Something to do with the streams.
     */
    public Loader then(ExceptionalConsumer<List<InputStream>> consumer) throws ConfigException {
        consumer.accept(streams);
        return this;
    }

    /**
     * Open a subdirectory.
     *
     * @param directory
     * @return
     */
    public abstract Loader open(String directory);

    /**
     * Close all InputStreams opened.
     */
    public Loader close() {
        streams.forEach(input -> {
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
