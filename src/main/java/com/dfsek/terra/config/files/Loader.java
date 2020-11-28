package com.dfsek.terra.config.files;

import com.dfsek.tectonic.exception.ConfigException;

import java.io.InputStream;
import java.util.List;

public interface Loader {
    /**
     * Do something with the InputStreams.
     *
     * @param consumer Something to do with the streams.
     */
    Loader then(ExceptionalConsumer<List<InputStream>> consumer) throws ConfigException;

    /**
     * Close all InputStreams opened.
     */
    void close();
}
