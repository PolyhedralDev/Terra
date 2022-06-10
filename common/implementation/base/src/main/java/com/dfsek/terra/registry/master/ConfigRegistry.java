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
import java.util.Objects;
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
    
    public ConfigRegistry() {
        super(TypeKey.of(ConfigPack.class));
    }
    
    public void load(File folder, Platform platform) throws ConfigException {
        ConfigPack pack = new ConfigPackImpl(folder, platform);
        registerChecked(pack.getRegistryKey(), pack);
    }
    
    public boolean loadAll(Platform platform) {
        boolean valid = true;
        File packsFolder = new File(platform.getDataFolder(), "packs");
        packsFolder.mkdirs();
        for(File dir : Objects.requireNonNull(packsFolder.listFiles(File::isDirectory))) {
            try {
                load(dir, platform);
            } catch(ConfigException e) {
                logger.error("Error loading config pack {}", dir.getName(), e);
                valid = false;
            }
        }
        for(File zip : Objects.requireNonNull(
                packsFolder.listFiles(file -> file.getName().endsWith(".zip") || file.getName().endsWith(".terra")))) {
            try {
                logger.info("Loading ZIP archive: {}", zip.getName());
                load(new ZipFile(zip), platform);
            } catch(IOException | ConfigException e) {
                logger.error("Error loading config pack {}", zip.getName(), e);
                valid = false;
            }
        }
        return valid;
    }
    
    public void load(ZipFile file, Platform platform) throws ConfigException {
        ConfigPackImpl pack = new ConfigPackImpl(file, platform);
        registerChecked(pack.getRegistryKey(), pack);
    }
}
