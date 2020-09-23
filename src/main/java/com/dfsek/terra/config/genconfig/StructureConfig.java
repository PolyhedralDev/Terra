package com.dfsek.terra.config.genconfig;

import com.dfsek.terra.config.TerraConfigObject;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;

public class StructureConfig extends TerraConfigObject {
    public StructureConfig(File file) throws IOException, InvalidConfigurationException {
        super(file);
    }

    @Override
    public void init() throws InvalidConfigurationException {

    }

    @Override
    public String getID() {
        return null;
    }
}
