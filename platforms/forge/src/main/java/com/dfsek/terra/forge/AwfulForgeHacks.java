package com.dfsek.terra.forge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarFile;
import java.util.stream.Stream;


/**
 * Forge is really wacky and screws with class resolution in the addon loader. Loading every single Terra class *manually* on startup
 * fixes it. If you know of a better way to fix this, PLEASE let us know.
 */
public final class AwfulForgeHacks {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwfulForgeHacks.class);
    
    /**
     * Forge tampers with code source to make the *normal* way of getting the current JAR file useless, so this awful hack is
     * needed.
     *
     * <code>
     *     Class.class.getProtectionDomain()
     *                .getCodeSource()
     *                .getLocation()
     *                .toURI()
     *                .getPath()
     * </code>
     *
     */
    public static JarFile getTerraJar() throws IOException {
        LOGGER.info("Scanning for Terra JAR...");
        return Files.walk(Path.of("./", "mods"), 1, FileVisitOption.FOLLOW_LINKS)
                    .filter(it -> it.getFileName().toString().endsWith(".jar"))
                    .peek(path -> LOGGER.info("Found mod: {}", path))
                    .map(Path::toFile)
                    .flatMap(path -> {
                        try {
                            return Stream.of(new JarFile(path));
                        } catch(IOException e) {
                            LOGGER.error("Malformed mod JAR: {}: {}", path, e);
                            return Stream.of();
                        }
                    })
                    .filter(jar -> jar
                            .stream()
                            .anyMatch(entry -> entry
                                    .getName()
                                    .equals(ForgeEntryPoint.class.getName().replace('.', '/') + ".class")))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Could not find Terra JAR"));

    }
    
    public static void loadAllTerraClasses() {
        
        try(JarFile jar = getTerraJar()) {
            jar.stream()
                    .forEach(jarEntry -> {
                        if(jarEntry.getName().startsWith("com/dfsek/terra/forge/mixin")) {
                            return;
                        }
                        if(jarEntry.getName().endsWith(".class")) {
                            String name = jarEntry.getName().replace('/', '.');
                            name = name.substring(0, name.length() - 6);
                            try {
                                Class.forName(name);
                            } catch(ClassNotFoundException | NoClassDefFoundError e) {
                                LOGGER.warn("Failed to load class {}: {}", name, e);
                            }
                        }
                    });
        } catch(IOException e) {
            throw new IllegalStateException("Could not load all Terra classes", e);
        }
    }
}
