package com.dfsek.terra.registry;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.terra.Debug;
import com.dfsek.terra.config.base.ConfigPack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

/**
 * Class to hold config packs
 */
public class ConfigRegistry extends TerraRegistry<ConfigPack> {
    private static ConfigRegistry singleton;

    private ConfigRegistry() {

    }

    public static ConfigRegistry getRegistry() {
        if(singleton == null) singleton = new ConfigRegistry();
        return singleton;
    }

    public void load(File folder) throws ConfigException {
        ConfigPack pack = new ConfigPack(folder);
        add(pack.getTemplate().getID(), pack);
    }

    public static boolean loadAll(JavaPlugin main) {
        boolean valid = true;
        File packsFolder = new File(main.getDataFolder(), "packs");
        for(File dir : packsFolder.listFiles(File::isDirectory)) {
            try {
                getRegistry().load(dir);
            } catch(ConfigException e) {
                e.printStackTrace();
                valid = false;
            }
        }
        for(File zip : packsFolder.listFiles(file -> file.getName().endsWith(".zip") || file.getName().endsWith(".jar") || file.getName().endsWith(".terra"))) {
            try {
                Debug.info("Loading ZIP archive: " + zip.getName());
                getRegistry().load(new ZipFile(zip));
            } catch(IOException | ConfigException e) {
                e.printStackTrace();
                valid = false;
            }
        }
        return valid;
    }

    public void load(ZipFile file) throws ConfigException {
        ConfigPack pack = new ConfigPack(file);
        add(pack.getTemplate().getID(), pack);
    }
}
