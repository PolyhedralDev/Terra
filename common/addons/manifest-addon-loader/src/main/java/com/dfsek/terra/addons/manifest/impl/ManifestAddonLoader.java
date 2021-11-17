package com.dfsek.terra.addons.manifest.impl;

import ca.solostudios.strata.version.Version;
import ca.solostudios.strata.version.VersionRange;
import com.dfsek.tectonic.exception.LoadException;
import com.dfsek.tectonic.loading.ConfigLoader;
import com.dfsek.tectonic.yaml.YamlConfiguration;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import com.dfsek.terra.addons.manifest.api.AddonInitializer;
import com.dfsek.terra.addons.manifest.impl.config.AddonManifest;
import com.dfsek.terra.addons.manifest.impl.config.WebsiteConfig;
import com.dfsek.terra.addons.manifest.impl.config.loaders.VersionLoader;
import com.dfsek.terra.addons.manifest.impl.config.loaders.VersionRangeLoader;
import com.dfsek.terra.addons.manifest.impl.exception.AddonException;
import com.dfsek.terra.addons.manifest.impl.exception.ManifestException;
import com.dfsek.terra.addons.manifest.impl.exception.ManifestNotPresentException;
import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.addon.bootstrap.BootstrapBaseAddon;
import com.dfsek.terra.api.inject.annotations.Inject;


public class ManifestAddonLoader implements BootstrapBaseAddon<ManifestAddon> {
    @Inject
    private Platform platform;
    
    @Override
    public Iterable<ManifestAddon> loadAddons(Path addonsFolder, ClassLoader parent) {
        platform.logger().info("Loading addons...");
        
        ConfigLoader manifestLoader = new ConfigLoader();
        manifestLoader.registerLoader(Version.class, new VersionLoader())
                      .registerLoader(VersionRange.class, new VersionRangeLoader())
                      .registerLoader(WebsiteConfig.class, WebsiteConfig::new);
        
        try {
            return Files.walk(addonsFolder, 1)
                        .filter(path -> path.toFile().isFile() && path.toString().endsWith(".jar"))
                        .map(path -> {
                            try {
                                platform.getDebugLogger().info("Loading addon from JAR " + path);
                                JarFile jar = new JarFile(path.toFile());
                    
                                JarEntry manifestEntry = jar.getJarEntry("terra.addon.yml");
                                if(manifestEntry == null) {
                                    throw new ManifestNotPresentException("Addon " + path + " does not contain addon manifest.");
                                }
                    
                    
                                try {
                                    AddonManifest manifest = manifestLoader.load(new AddonManifest(),
                                                                                 new YamlConfiguration(jar.getInputStream(manifestEntry),
                                                                                                       "terra.addon.yml"));
                        
                                    platform.logger().info("Loading addon " + manifest.getID());
                        
                                    ManifestAddonClassLoader loader = new ManifestAddonClassLoader(new URL[]{ path.toUri().toURL() },
                                                                                                   getClass().getClassLoader());
                        
                                    return new ManifestAddon(manifest, manifest.getEntryPoints().stream().map(entryPoint -> {
                                        try {
                                            Object in = loader.loadClass(entryPoint).getConstructor().newInstance();
                                            if(!(in instanceof AddonInitializer)) {
                                                throw new AddonException(in.getClass() + " does not extend " + AddonInitializer.class);
                                            }
                                            return (AddonInitializer) in;
                                        } catch(InvocationTargetException e) {
                                            throw new AddonException("Exception occurred while instantiating addon: ", e);
                                        } catch(NoSuchMethodException | IllegalAccessException | InstantiationException e) {
                                            throw new AddonException("No valid default constructor found in entry point " + entryPoint);
                                        } catch(ClassNotFoundException e) {
                                            throw new AddonException("Entry point " + entryPoint + " not found in JAR.");
                                        }
                                    }).collect(Collectors.toList()));
                        
                                } catch(LoadException e) {
                                    throw new ManifestException("Failed to load addon manifest", e);
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
        return "MANIFEST";
    }
}
