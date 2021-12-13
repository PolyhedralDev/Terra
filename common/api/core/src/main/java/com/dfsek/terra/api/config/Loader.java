/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.config;

import com.dfsek.tectonic.api.exception.ConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;


public interface Loader {
    
    Loader thenNames(Consumer<List<String>> consumer) throws ConfigException;
    
    Loader thenEntries(Consumer<Set<Map.Entry<String, InputStream>>> consumer) throws ConfigException;
    
    /**
     * Get a single file from this Loader.
     *
     * @param singleFile File to get
     *
     * @return InputStream from file.
     */
    InputStream get(String singleFile) throws IOException;
    
    /**
     * Open a subdirectory.
     *
     * @param directory Directory to open
     * @param extension File extension
     */
    Loader open(String directory, String extension);
    
    /**
     * Close all InputStreams opened.
     */
    Loader close();
}
