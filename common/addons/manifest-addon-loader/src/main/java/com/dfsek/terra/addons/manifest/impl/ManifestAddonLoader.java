/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.manifest.impl;

import ca.solostudios.strata.Versions;
import ca.solostudios.strata.version.Version;
import ca.solostudios.strata.version.VersionRange;
import com.dfsek.tectonic.api.exception.LoadException;
import com.dfsek.tectonic.api.loader.ConfigLoader;
import com.dfsek.tectonic.yaml.YamlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.addons.manifest.impl.config.AddonManifest;
import com.dfsek.terra.addons.manifest.impl.config.WebsiteConfig;
import com.dfsek.terra.addons.manifest.impl.config.loaders.VersionLoader;
import com.dfsek.terra.addons.manifest.impl.config.loaders.VersionRangeLoader;
import com.dfsek.terra.addons.manifest.impl.exception.AddonException;
import com.dfsek.terra.addons.manifest.impl.exception.ManifestException;
import com.dfsek.terra.addons.manifest.impl.exception.ManifestNotPresentException;
import com.dfsek.terra.api.addon.bootstrap.BootstrapAddonClassLoader;
import com.dfsek.terra.api.addon.bootstrap.BootstrapBaseAddon;


public class ManifestAddonLoader implements BootstrapBaseAddon<ManifestAddon> {
    private static final Logger logger = LoggerFactory.getLogger(ManifestAddonLoader.class);
    private static final Version VERSION = Versions.getVersion(1, 0, 0);
    
    private final ConfigLoader manifestLoader = new ConfigLoader()
            .registerLoader(Version.class, new VersionLoader())
            .registerLoader(VersionRange.class, new VersionRangeLoader())
            .registerLoader(WebsiteConfig.class, WebsiteConfig::new);
    
    public ManifestAddon loadAddon(Path addonPath, ClassLoader loader) {
        try(JarFile jar = new JarFile(addonPath.toFile())) {
            logger.debug("Loading addon from JAR {}", addonPath);
            
            JarEntry manifestEntry = jar.getJarEntry("terra.addon.yml");
            if(manifestEntry == null) {
                throw new ManifestNotPresentException("Addon " + addonPath + " does not contain addon manifest.");
            }
            
            
            //noinspection NestedTryStatement
            try {
                AddonManifest manifest = manifestLoader.load(new AddonManifest(),
                                                             new YamlConfiguration(jar.getInputStream(manifestEntry),
                                                                                   "terra.addon.yml"));
                
                logger.debug("Loading addon {}@{}", manifest.getID(), manifest.getVersion().getFormatted());
                
                if(manifest.getSchemaVersion() != 1) {
                    throw new AddonException("Addon " + manifest.getID() + " has unknown schema version: " + manifest.getSchemaVersion());
                }
                
                List<AddonInitializer> initializers = manifest.getEntryPoints().stream().map(entryPoint -> {
                    try {
                        Object in = loader.loadClass(entryPoint).getConstructor().newInstance();
                        if(!(in instanceof AddonInitializer)) {
                            throw new AddonException(in.getClass() + " does not extend " + AddonInitializer.class);
                        }
                        return (AddonInitializer) in;
                    } catch(InvocationTargetException e) {
                        throw new AddonException("Exception occurred while instantiating addon", e);
                    } catch(NoSuchMethodException | IllegalAccessException | InstantiationException e) {
                        throw new AddonException(String.format("No valid default constructor found in entry point %s", entryPoint), e);
                    } catch(ClassNotFoundException e) {
                        throw new AddonException(String.format("Entry point %s not found in JAR.", entryPoint), e);
                    }
                }).collect(Collectors.toList());
                
                return new ManifestAddon(manifest, initializers);
                
            } catch(LoadException e) {
                throw new ManifestException("Failed to load addon manifest", e);
            }
        } catch(IOException e) {
            throw new AddonException("Failed to load addon from JAR " + addonPath, e);
        }
    }
    
    @Override
    public Iterable<ManifestAddon> loadAddons(Path addonsFolder, BootstrapAddonClassLoader parent) {
        logger.debug("Loading addons...");
        
        try(Stream<Path> files = Files.walk(addonsFolder, 1, FileVisitOption.FOLLOW_LINKS)) {
            List<Path> addons = files
                    .filter(path -> path.toFile().isFile())
                    .filter(path -> path.toFile().canRead())
                    .filter(path -> !path.getFileName().startsWith(".")) // ignore hidden files.
                    .filter(path -> path.toString().endsWith(".jar"))
                    .toList();
            
            addons.stream().map(path -> {
                try {
                    return path.toUri().toURL();
                } catch(MalformedURLException e) {
                    throw new UncheckedIOException(e);
                }
            }).forEach(parent::addURL);
            
            return addons.stream()
                         .map(jar -> loadAddon(jar, parent))
                         .collect(Collectors.toList());
        } catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    
    @Override
    public String getID() {
        return "MANIFEST";
    }
    
    @Override
    public Version getVersion() {
        return VERSION;
    }
}
