package com.dfsek.terra.api.config;

import com.dfsek.tectonic.config.Configuration;
import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.terra.api.util.function.ExceptionalConsumer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Loader {

    Loader thenNames(ExceptionalConsumer<List<String>> consumer) throws ConfigException;

    Loader thenEntries(ExceptionalConsumer<Set<Map.Entry<String, InputStream>>> consumer) throws ConfigException;

    /**
     * Get a single file from this Loader.
     *
     * @param singleFile File to get
     * @return InputStream from file.
     */
    InputStream get(String singleFile) throws IOException;

    /**
     * Open a subdirectory.
     *
     * @param directory Directory to open
     * @param extension
     */
    Loader open(String directory, String extension);

    /**
     * Close all InputStreams opened.
     */
    Loader close();
}
