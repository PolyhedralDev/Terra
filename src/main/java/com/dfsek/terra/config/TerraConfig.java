package com.dfsek.terra.config;

import com.dfsek.terra.config.base.ConfigPack;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class TerraConfig extends YamlConfiguration {
    private final ConfigPack config;
    public TerraConfig(File file, ConfigPack config) throws IOException, InvalidConfigurationException {
        load(file);
        this.config = config;
    }

    public ConfigPack getConfig() {
        return config;
    }

    public abstract String getID();
}
