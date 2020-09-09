package com.dfsek.terra.config;

import com.dfsek.terra.biome.UserDefinedBiome;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.polydev.gaea.math.parsii.tokenizer.ParseException;

import java.io.File;
import java.io.IOException;

public class BiomeConfig extends YamlConfiguration {
    private final UserDefinedBiome biome;
    private final String biomeID;
    private final String friendlyName;
    public BiomeConfig(File file) throws InvalidConfigurationException, ParseException, IOException {
        super();
        super.load(file);
        if(!contains("noise-equation")) throw new InvalidConfigurationException("No noise equation included in biome!");
        this.biome = new UserDefinedBiome(getString("noise-equation"));
        if(!contains("id")) throw new InvalidConfigurationException("Biome ID unspecified!");
        this.biomeID = getString("id");
        if(!contains("name")) throw new InvalidConfigurationException("Biome Name unspecified!");
        this.friendlyName = getString("name");
    }

    public UserDefinedBiome getBiome() {
        return biome;
    }

    public String getBiomeID() {
        return biomeID;
    }

    public String getFriendlyName() {
        return friendlyName;
    }
}
