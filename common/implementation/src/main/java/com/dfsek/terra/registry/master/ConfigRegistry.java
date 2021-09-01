package com.dfsek.terra.registry.master;

import com.dfsek.tectonic.exception.ConfigException;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.config.pack.ConfigPackImpl;
import com.dfsek.terra.registry.OpenRegistryImpl;


/**
 * Class to hold config packs
 */
public class ConfigRegistry extends OpenRegistryImpl<ConfigPack> {
    public void load(File folder, TerraPlugin main) throws ConfigException {
        ConfigPack pack = new ConfigPackImpl(folder, main);
        register(pack.getID(), pack);
    }
    
    public boolean loadAll(TerraPlugin main) {
        boolean valid = true;
        File packsFolder = new File(main.getDataFolder(), "packs");
        packsFolder.mkdirs();
        for(File dir : packsFolder.listFiles(File::isDirectory)) {
            try {
                load(dir, main);
            } catch(ConfigException e) {
                e.printStackTrace();
                valid = false;
            }
        }
        for(File zip : packsFolder.listFiles(file -> file.getName().endsWith(".zip") || file.getName().endsWith(".terra"))) {
            try {
                main.getDebugLogger().info("Loading ZIP archive: " + zip.getName());
                load(new ZipFile(zip), main);
            } catch(IOException | ConfigException e) {
                e.printStackTrace();
                valid = false;
            }
        }
        return valid;
    }
    
    public void load(ZipFile file, TerraPlugin main) throws ConfigException {
        ConfigPackImpl pack = new ConfigPackImpl(file, main);
        register(pack.getTemplate().getID(), pack);
    }
}
