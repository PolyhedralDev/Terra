package com.dfsek.terra.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class TerraConfigObject extends YamlConfiguration {
    private final TerraConfig config;
    public TerraConfigObject(File file, TerraConfig config) throws IOException, InvalidConfigurationException {
        load(file);
        this.config = config;
    }

    public TerraConfig getConfig() {
        return config;
    }

    public abstract String getID();
}
