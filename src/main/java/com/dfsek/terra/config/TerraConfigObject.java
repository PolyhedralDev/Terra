package com.dfsek.terra.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class TerraConfigObject extends YamlConfiguration {
    public TerraConfigObject(File file) throws IOException, InvalidConfigurationException {
        super.load(file);
        init();
    }
    public abstract void init() throws InvalidConfigurationException;
    public abstract String getID();
}
