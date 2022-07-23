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

package com.dfsek.terra.registry.master;

import com.dfsek.tectonic.api.exception.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.ZipFile;

import com.dfsek.terra.api.Platform;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.util.reflection.TypeKey;
import com.dfsek.terra.config.pack.ConfigPackImpl;
import com.dfsek.terra.registry.OpenRegistryImpl;


/**
 * Class to hold config packs
 */
public class ConfigRegistry extends OpenRegistryImpl<ConfigPack> {
    private static final Logger logger = LoggerFactory.getLogger(ConfigRegistry.class);
    
    private static final String PACK_MANIFEST_NAME = "pack.yml";
    
    private static final String PACKS_FOLDER ="packs";
    
    public ConfigRegistry() {
        super(TypeKey.of(ConfigPack.class));
    }
    
    public boolean load(File folder, Platform platform) {
        try {
            ConfigPack pack = new ConfigPackImpl(folder, platform);
            registerChecked(pack.getRegistryKey(), pack);
            return true;
        } catch(ConfigException e) {
            logger.error("Error loading config pack {}", folder.getName(), e);
            return false;
        }
    }
    
    public boolean load(ZipFile zip, Platform platform) throws ConfigException {
        try {
            ConfigPack pack = new ConfigPackImpl(zip, platform);
            registerChecked(pack.getRegistryKey(), pack);
            return true;
        } catch(ConfigException e) {
            logger.error("Error loading config pack {}", zip.getName(), e);
            return false;
        }
    }
    
    public boolean loadPacksFromZip(Path zip) {
        return true;
    }
    
    private boolean hasManifest(File folder) {
        return new File(folder, PACK_MANIFEST_NAME).exists();
    }
    
    private boolean hasManifest(ZipFile folder) {
        return folder.getEntry(PACK_MANIFEST_NAME) != null;
    }
    
    public boolean loadAll(Platform platform) {
        File packsFolder = new File(platform.getDataFolder(), PACKS_FOLDER);
        packsFolder.mkdirs();
        boolean valid = true;
        
        // Breadth first search through the packs folder for config packs
        Queue<File> queue = new LinkedList<>();
        queue.add(packsFolder);
        while (!queue.isEmpty()) {
            File current = queue.poll();
            logger.debug("Checking for packs in {}", current.getPath());
            File[] files = current.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        if (hasManifest(file)) {
                            logger.debug("Loading {} as a pack", file.getPath());
                            if(!load(file, platform)) valid = false;
                        } else {
                            queue.add(file); // Recurse if folder does not contain a manifest
                        }
                    } else if (file.getName().endsWith(".zip") || file.getName().endsWith(".terra")) {
                        try (ZipFile zip = new ZipFile(file)) {
                            if (hasManifest(zip)) {
                                logger.debug("Loading ZIP archive {} as a pack", zip.getName());
                                if(!load(zip, platform)) valid = false;
                            } else {
                                // Recurse if zip does not contain a manifest
                                logger.debug("Searching for packs in ZIP archive {}", file.getPath());
                                if(!loadPacksFromZip(file.toPath())) valid = false;
                            }
                        } catch(IOException e) {
                            logger.error("Failed to load ZIP archive: {}", file.getPath(), e);
                            valid = false;
                        }
                    }
                }
            }
        }
        return valid;
    }
}
