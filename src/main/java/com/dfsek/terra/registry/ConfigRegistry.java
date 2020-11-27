package com.dfsek.terra.registry;

import com.dfsek.tectonic.exception.ConfigException;
import com.dfsek.terra.config.base.ConfigPack;

import java.io.File;

/**
 * Class to hold config packs
 */
public class ConfigRegistry extends TerraRegistry<ConfigPack> {
    public void load(File folder) throws ConfigException {
        ConfigPack pack = new ConfigPack(folder);
        add(pack.getTemplate().getID(), pack);
    }
}
