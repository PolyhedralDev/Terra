package com.dfsek.terra.registry;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.terra.Terra;
import com.dfsek.terra.config.base.ConfigPack;

import java.io.File;

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

    public static void loadAll(Terra main) {
        File packsFolder = new File(main.getDataFolder(), "packs");
        for(File dir : packsFolder.listFiles(File::isDirectory)) {
            try {
                getRegistry().load(dir);
            } catch(ConfigException e) {
                e.printStackTrace();
            }
        }
    }
}
