package com.dfsek.terra.config;

import com.dfsek.terra.Terra;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.structures.NMSStructure;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class StructureConfig extends YamlConfiguration {
    private String id;
    private String name;
    private Object structure;
    private int offset;
    public StructureConfig(File file) throws IOException, InvalidConfigurationException {
        this.load(file);
    }

    @Override
    public void load(@NotNull File file) throws IOException, InvalidConfigurationException {
        super.load(file);
        if(!contains("id")) throw new InvalidConfigurationException("Structure ID unspecified!");
        this.id = getString("id");
        if(!contains("name")) throw new InvalidConfigurationException("Biome Name unspecified!");
        this.name = getString("name");
        this.offset = getInt("offset", 0);
        try {
            structure = NMSStructure.getAsTag(new FileInputStream(new File(Terra.getInstance().getDataFolder() + File.separator + "structures" + File.separator + "nbt" + File.separator + getString("file"))));
        } catch(FileNotFoundException e) {
            throw new InvalidConfigurationException("Unable to locate structure file Terra/structures/nbt/" + getString("file"));
        }
    }
    public NMSStructure getInstance(Location origin) {
        return new NMSStructure(origin, structure);
    }
}
