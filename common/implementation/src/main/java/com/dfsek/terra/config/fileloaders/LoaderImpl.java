package com.dfsek.terra.config.fileloaders;

import com.dfsek.tectonic.exception.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import com.dfsek.terra.api.config.Loader;


public abstract class LoaderImpl implements Loader {
    private static final Logger logger = LoggerFactory.getLogger(LoaderImpl.class);
    
    protected final Map<String, InputStream> streams = new HashMap<>();
    
    @Override
    public Loader thenNames(Consumer<List<String>> consumer) throws ConfigException {
        consumer.accept(new ArrayList<>(streams.keySet()));
        return this;
    }
    
    @Override
    public Loader thenEntries(Consumer<Set<Map.Entry<String, InputStream>>> consumer) throws ConfigException {
        consumer.accept(streams.entrySet());
        return this;
    }
    
    /**
     * Open a subdirectory.
     *
     * @param directory Directory to open
     * @param extension
     */
    @Override
    public LoaderImpl open(String directory, String extension) {
        if(streams.size() != 0) throw new IllegalStateException("Attempted to load new directory before closing existing InputStreams");
        load(directory, extension);
        return this;
    }
    
    /**
     * Close all InputStreams opened.
     */
    @Override
    public Loader close() {
        streams.forEach((name, input) -> {
            try {
                input.close();
            } catch(IOException e) {
                logger.warn("Error occurred while loading", e);
            }
        });
        streams.clear();
        return this;
    }
    
    protected abstract void load(String directory, String extension);
}
