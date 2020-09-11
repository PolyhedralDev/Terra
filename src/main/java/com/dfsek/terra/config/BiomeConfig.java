package com.dfsek.terra.config;

import com.dfsek.terra.biome.UserDefinedBiome;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.math.parsii.tokenizer.ParseException;

import java.io.File;
import java.io.IOException;

public class BiomeConfig extends YamlConfiguration {
    private UserDefinedBiome biome;
    private String biomeID;
    private String friendlyName;
    private org.bukkit.block.Biome vanillaBiome;
    private boolean isEnabled = false;

    public BiomeConfig(File file) throws InvalidConfigurationException, IOException {
        super();
        load(file);
    }


    @Override
    public void load(@NotNull File file) throws InvalidConfigurationException, IOException {
        isEnabled = false;
        super.load(file);
        if(!contains("noise-equation")) throw new InvalidConfigurationException("No noise equation included in biome!");
        try {
            this.biome = new UserDefinedBiome(this);
        } catch(ParseException e) {
            e.printStackTrace();
            Bukkit.getLogger().severe("Unable to parse noise equation!");
        }
        if(!contains("id")) throw new InvalidConfigurationException("Biome ID unspecified!");
        this.biomeID = getString("id");
        if(!contains("name")) throw new InvalidConfigurationException("Biome Name unspecified!");
        this.friendlyName = getString("name");
        if(!contains("vanilla")) throw new InvalidConfigurationException("Vanila Biome unspecified!");
        try {
            this.vanillaBiome = org.bukkit.block.Biome.valueOf(getString("vanilla"));
        } catch(IllegalArgumentException e) {
            throw new InvalidConfigurationException("Invalid Vanilla biome: " + getString("vanilla"));
        }
        isEnabled = true;
    }

    public boolean isEnabled() {
        return isEnabled;
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

    public org.bukkit.block.Biome getVanillaBiome() {
        return vanillaBiome;
    }
}
