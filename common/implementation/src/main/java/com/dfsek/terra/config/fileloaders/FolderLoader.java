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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class FolderLoader extends LoaderImpl {
    private static final Logger logger = LoggerFactory.getLogger(FolderLoader.class);
    
    private final Path path;
    
    public FolderLoader(Path path) {
        this.path = path;
    }
    
    @Override
    public InputStream get(String singleFile) throws IOException {
        return new FileInputStream(new File(path.toFile(), singleFile));
    }
    
    protected void load(String directory, String extension) {
        File newPath = new File(path.toFile(), directory);
        newPath.mkdirs();
        try(Stream<Path> paths = Files.walk(newPath.toPath())) {
            paths.filter(Files::isRegularFile).filter(file -> file.toString().toLowerCase().endsWith(extension)).forEach(file -> {
                try {
                    String rel = newPath.toPath().relativize(file).toString();
                    streams.put(rel, new FileInputStream(file.toFile()));
                } catch(FileNotFoundException e) {
                    logger.error("Could not find file to load", e);
                }
            });
        } catch(IOException e) {
            logger.error("Error while loading files", e);
        }
    }
}
