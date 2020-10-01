package com.dfsek.terra.config.genconfig;

import org.polydev.gaea.math.Range;
import com.dfsek.terra.Terra;
import com.dfsek.terra.config.ConfigUtil;
import com.dfsek.terra.config.TerraConfigObject;
import com.dfsek.terra.population.StructurePopulator;
import com.dfsek.terra.structure.GaeaStructure;
import com.dfsek.terra.procgen.GridSpawn;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StructureConfig extends TerraConfigObject {
    private static final Map<String, StructureConfig> configs = new HashMap<>();
    private GaeaStructure structure;
    private GridSpawn spawn;
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
            File file = new File(Terra.getInstance().getDataFolder() + File.separator + "config" + File.separator + "structures" + File.separator + "data", Objects.requireNonNull(getString("file")));
            structure = GaeaStructure.load(file);
        } catch(IOException | NullPointerException e) {
            if(ConfigUtil.debug) {
                e.printStackTrace();
            }
            throw new InvalidConfigurationException("Unable to locate structure: " + getString("file"));
        }
        if(!contains("id")) throw new InvalidConfigurationException("No ID specified!");
        id = getString("id");
        spawn = new GridSpawn(getInt("spawn.width", 500), getInt("spawn.padding", 100));
        searchStart = new Range(getInt("spawn.start.min", 72), getInt("spawn.start.max", 72));
        bound = new Range(getInt("spawn.bound.min", 48), getInt("spawn.bound.max", 72));
        try {
            type = StructurePopulator.SearchType.valueOf(getString("spawn.search", "DOWN"));
        } catch(IllegalArgumentException e) {
            throw new InvalidConfigurationException("Invalid search type, " + getString("spawn.search"));
        }
        configs.put(id, this);
    }

    @Override
    public String getID() {
        return id;
    }

    public GaeaStructure getStructure() {
        return structure;
    }

    public GridSpawn getSpawn() {
        return spawn;
    }

    public Range getBound() {
        return bound;
    }

    public Range getSearchStart() {
        return searchStart;
    }

    public static StructureConfig fromID(String id) {
        return configs.get(id);
    }
}
