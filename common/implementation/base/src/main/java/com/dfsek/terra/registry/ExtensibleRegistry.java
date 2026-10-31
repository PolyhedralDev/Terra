package com.dfsek.terra.registry;

import com.google.errorprone.annotations.MustBeClosed;
import org.intellij.lang.annotations.Pattern;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;


/**
 * A file-discoverable registry that can be extended by system properties
 * <br>
 * The main use-case for this is to allow for customized locations of file-system-based key entries,
 * such as putting config packs into custom locations.
 * <br>
 * Two system properties are used to define the search path:
 * <br>
 * <code>terra.registry.{searchPathName}.searchPath</code> is a list of "parent folder" paths (like the pack directory) that
 * will be searched for contained files.
 * <br>
 * <code>terra.registry.{searchPathName}.extraPath</code> is a list of files that will be directly evaluated for membership and
 * as such will be directly treated as members of the registry.
 * <br>
 * These system properties are formatted like UNIX-style paths, with each path separated by a colon (':') character.
 */
public interface ExtensibleRegistry {
    /**
     * Get a lowercase search path name for this registry that will get plugged into
     * <br>
     * <code>terra.registry.{searchPathName}.searchPath</code>
     * <br>
     * and
     * <br>
     * <code>terra.registry.{searchPathName}.extraPath</code>
     */
    @Pattern("^[a-z._]+$")
    String getRegistryName();

    /**
     * Check if a path may be a member of this registry heuristically.
     *
     * @param path Path to check
     *
     * @return True if the path is a member of this registry.
     */
    boolean validatePathIsMember(Path path);

    default @MustBeClosed Stream<Path> getMemberPaths(Path baseSearchPath) {
        String basePropertyName = "terra.registry." + getRegistryName();

        Stream<Path> searchPath = Stream.concat(
            parseValidPaths(System.getProperty(basePropertyName + ".search-path")),
            Stream.of(baseSearchPath)
        );

        Stream<Path> extraPath = parseValidPaths(System.getProperty(basePropertyName + ".extra-path"));

        return Stream.concat(
                searchPath
                    .filter(Files::isDirectory)
                    .flatMap(ExtensibleRegistry::listDirectory),
                extraPath
            )
            .filter(this::validatePathIsMember);
    }

    private static Stream<Path> parseValidPaths(@Nullable String paths) {
        return Stream.ofNullable(paths)
            .flatMap(p -> Arrays.stream(p.split(":")))
            .filter(v -> !v.isBlank())
            .map(v -> {
                try {
                    return Path.of(v).toAbsolutePath().normalize();
                } catch (Exception e) {
                    return null;
                }
            })
            .filter(Objects::nonNull);
    }

    private static Stream<Path> listDirectory(Path dir) {
        try {
            return Files.list(dir);
        } catch(IOException e) {
            return Stream.empty();
        }
    }
}
