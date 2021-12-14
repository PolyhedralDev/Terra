/*
 * This file is part of Terra.
 *
 * Terra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Terra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Terra.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.dfsek.terra.config.fileloaders;

import com.dfsek.tectonic.api.exception.ConfigException;
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
     * @param extension File extension
     */
    @Override
    public LoaderImpl open(String directory, String extension) {
        if(!streams.isEmpty()) throw new IllegalStateException("Attempted to load new directory before closing existing InputStreams");
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
                logger.error("Error occurred while loading", e);
            }
        });
        streams.clear();
        return this;
    }
    
    protected abstract void load(String directory, String extension);
}
