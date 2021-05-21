package com.dfsek.terra.registry.master;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.terra.api.TerraPlugin;
import com.dfsek.terra.config.pack.ConfigPack;
import com.dfsek.terra.registry.OpenRegistry;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

/**
 * Class to hold config packs
 */
public class ConfigRegistry extends OpenRegistry<ConfigPack> {
    public void load(File folder, TerraPlugin main) throws ConfigException {
        ConfigPack pack = new ConfigPack(folder, main);
        add(pack.getTemplate().getID(), pack);
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
        ConfigPack pack = new ConfigPack(file, main);
        add(pack.getTemplate().getID(), pack);
    }
}
