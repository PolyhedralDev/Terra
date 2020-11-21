package com.dfsek.terra.config;

import com.dfsek.terra.config.base.ConfigPack;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@SuppressWarnings("unused")
public abstract class TerraConfig {
    protected final YamlConfiguration yaml;
    private final ConfigPack config;
    protected String id;

    /**
     * Constructs a new Terra config with a file, config pack and Id.
     *
     * @param file   The file to use when constructing the config.
     * @param config The config reference.
     * @param id     The id of the object.
     * @throws IOException
     * @throws InvalidConfigurationException
     * @deprecated Deprecated because you should use {@link #TerraConfig(InputStream, ConfigPack, String)}
     */
    @Deprecated
    public TerraConfig(File file, ConfigPack config) throws IOException, InvalidConfigurationException {
        this(new FileInputStream(file), config);
    }

    /**
     * Constructs a new Terra config with an input stream, config pack and Id.
     *
     * @param stream The input stream to use when constructing the config.
     * @param config The config reference.
     * @param id     The id of the object.
     * @throws IOException
     * @throws InvalidConfigurationException
     */
    public TerraConfig(InputStream stream, ConfigPack config) throws IOException, InvalidConfigurationException {
        yaml = new YamlConfiguration();
        yaml.load(new InputStreamReader(stream));
        this.config = config;
    }

    public YamlConfiguration getYaml() {
        return yaml;
    }

    public ConfigPack getConfig() {
        return config;
    }

    public String getID() {
        return id;
    }
}
