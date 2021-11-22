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

package com.dfsek.terra.addon;

import ca.solostudios.strata.Versions;
import ca.solostudios.strata.version.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import com.dfsek.terra.addon.exception.AddonLoadException;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.bootstrap.BootstrapBaseAddon;


public class BootstrapAddonLoader implements BootstrapBaseAddon<BootstrapBaseAddon<?>> {
    private static final Logger logger = LoggerFactory.getLogger(BootstrapAddonLoader.class);
    private static final Version VERSION = Versions.getVersion(1, 0, 0);
    
    private final Platform platform;
    
    public BootstrapAddonLoader(Platform platform) { this.platform = platform; }
    
    @Override
    public Iterable<BootstrapBaseAddon<?>> loadAddons(Path addonsFolder, ClassLoader parent) {
        Path bootstrapAddons = addonsFolder.resolve("bootstrap");
        logger.info("Loading bootstrap addons from {}", bootstrapAddons);
        try {
            return Files.walk(bootstrapAddons, 1)
                        .filter(path -> path.toFile().isFile() && path.toString().endsWith(".jar"))
                        .map(path -> {
                            try {
                                logger.info("Loading bootstrap addon from JAR {}", path);
                                JarFile jar = new JarFile(path.toFile());
                                String entry = jar.getManifest().getMainAttributes().getValue("Bootstrap-Addon-Entry-Point");
                    
                                if(entry == null) {
                                    throw new AddonLoadException("No Bootstrap-Addon-Entry-Point attribute defined in addon manifest.");
                                }
                    
                                AddonClassLoader loader = new AddonClassLoader(new URL[]{ path.toUri().toURL() }, parent);
                    
                                try {
                                    Object in = loader.loadClass(entry).getConstructor().newInstance();
                                    if(!(in instanceof BootstrapBaseAddon)) {
                                        throw new AddonLoadException(in.getClass() + " does not extend " + BootstrapBaseAddon.class);
                                    }
                                    logger.info("Loaded bootstrap addon {}", ((BootstrapBaseAddon<?>) in).getID());
                                    return (BootstrapBaseAddon<?>) in;
                                } catch(InvocationTargetException e) {
                                    throw new AddonLoadException("Exception occurred while instantiating addon: ", e);
                                } catch(NoSuchMethodException | IllegalAccessException | InstantiationException e) {
                                    throw new AddonLoadException("No valid default constructor found in entry point " + entry);
                                } catch(ClassNotFoundException e) {
                                    throw new AddonLoadException("Entry point " + entry + " not found in JAR.");
                                }
                    
                            } catch(IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        }).collect(Collectors.toList());
        } catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    @Override
    public String getID() {
        return "BOOTSTRAP";
    }
    
    @Override
    public Version getVersion() {
        return VERSION;
    }
}
