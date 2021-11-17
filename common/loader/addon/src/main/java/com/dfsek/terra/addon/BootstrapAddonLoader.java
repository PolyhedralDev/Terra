package com.dfsek.terra.addon;

import com.dfsek.terra.addon.exception.AddonLoadException;
import com.dfsek.terra.api.addon.bootstrap.BootstrapBaseAddon;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarFile;
import java.util.stream.Collectors;


public class BootstrapAddonLoader implements BootstrapBaseAddon<BootstrapBaseAddon<?>> {
    @Override
    public Iterable<BootstrapBaseAddon<?>> loadAddons(Path addonsFolder, ClassLoader parent) {
        Path bootstrapAddons = addonsFolder.resolve("bootstrap");
        
        try {
            return Files.walk(bootstrapAddons, 1)
                        .filter(path -> path.toFile().isFile() && path.getFileName().endsWith(".jar"))
                        .map(path -> {
                            try {
                                JarFile jar = new JarFile(path.toFile());
                                String entry = jar.getManifest().getMainAttributes().getValue("Bootstrap-Addon-Entry-Point");
                                AddonClassLoader loader = new AddonClassLoader(new URL[] {path.toUri().toURL()}, parent);
    
                                try {
                                    Object in = loader.loadClass(entry).getConstructor().newInstance();
                                    if(!(in instanceof BootstrapBaseAddon)) {
                                        throw new AddonLoadException(in.getClass() + " does not extend " + BootstrapBaseAddon.class);
                                    }
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
}
