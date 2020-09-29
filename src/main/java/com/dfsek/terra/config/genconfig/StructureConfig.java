package com.dfsek.terra.config.genconfig;

import com.dfsek.terra.Range;
import com.dfsek.terra.Terra;
import com.dfsek.terra.config.TerraConfigObject;
import com.dfsek.terra.population.StructurePopulator;
import com.dfsek.terra.structure.GaeaStructure;
import com.dfsek.terra.structure.StructureSpawn;
import com.dfsek.terra.structure.StructureSpawnRequirement;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class StructureConfig extends TerraConfigObject {
    private GaeaStructure structure;
    private StructureSpawn spawn;
    private String id;
    private Range searchStart;
    private Range bound;
    StructurePopulator.SearchType type;
    public StructureConfig(File file) throws IOException, InvalidConfigurationException {
        super(file);
    }

    @Override
    public void init() throws InvalidConfigurationException {
        try {
            structure = GaeaStructure.load(new File(Terra.getInstance().getDataFolder() + File.separator + "config" + File.separator + "structures" + File.separator + "data", Objects.requireNonNull(getString("file"))));
        } catch(IOException | NullPointerException e) {
            throw new InvalidConfigurationException("Unable to locate structure: " + getString("file"));
        }
        if(!contains("id")) throw new InvalidConfigurationException("No ID specified!");
        id = getString("id");
        spawn = new StructureSpawn(getInt("spawn.width", 500), getInt("spawn.padding", 100));
        searchStart = new Range(getInt("spawn.start.min", 72), getInt("spawn.start.max", 72));
        bound = new Range(getInt("spawn.bound.min", 48), getInt("spawn.bound.max", 72));
        try {
            type = StructurePopulator.SearchType.valueOf(getString("spawn,search", "DOWN"));
        } catch(IllegalArgumentException e) {
            throw new InvalidConfigurationException("Invalid search type, " + getString("spawn,search"));
        }
    }

    @Override
    public String getID() {
        return id;
    }

    public GaeaStructure getStructure() {
        return structure;
    }

    public StructureSpawn getSpawn() {
        return spawn;
    }

    public Range getBound() {
        return bound;
    }

    public Range getSearchStart() {
        return searchStart;
    }
}
