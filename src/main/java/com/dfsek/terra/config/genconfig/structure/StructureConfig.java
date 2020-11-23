package com.dfsek.terra.config.genconfig.structure;

import com.dfsek.terra.Debug;
import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.config.exception.NotFoundException;
import com.dfsek.terra.procgen.GridSpawn;
import com.dfsek.terra.structure.Structure;
import com.dfsek.terra.structure.features.Feature;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.json.simple.parser.ParseException;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.math.Range;
import org.polydev.gaea.structures.loot.LootTable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class StructureConfig extends TerraConfig {
    private final ProbabilityCollection<Structure> structure = new ProbabilityCollection<>();
    private final GridSpawn spawn;
    private final String id;
    private final Range searchStart;
    private final Range bound;
    private final Map<Integer, LootTable> loot = new Object2ObjectOpenHashMap<>();
    private final List<Feature> features;

    @SuppressWarnings("unchecked")
    public StructureConfig(File file, ConfigPack config) throws IOException, InvalidConfigurationException {
        super(file, config);
        if(!contains("id")) throw new ConfigException("No ID specified!", "null");
        id = getString("id");
        if(!contains("files")) throw new ConfigException("No files specified!", getID());
        try {
            for(Map.Entry<String, Object> e : Objects.requireNonNull(getConfigurationSection("files")).getValues(false).entrySet()) {
                try {
                    File structureFile = new File(config.getDataFolder() + File.separator + "structures" + File.separator + "data", e.getKey() + ".tstructure");
                    structure.add(Structure.load(structureFile), (Integer) e.getValue());
                } catch(FileNotFoundException ex) {
                    Debug.stack(ex);
                    throw new NotFoundException("Structure File", e.getKey(), getID());
                } catch(ClassCastException ex) {
                    Debug.stack(ex);
                    throw new ConfigException("Unable to parse Structure configuration! Check YAML syntax.", getID());
                }
            }
        } catch(IOException | NullPointerException e) {
            if(ConfigUtil.debug) {
                e.printStackTrace();
            }
            throw new NotFoundException("Structure", getString("file"), getID());
        }
        if(contains("loot")) {
            for(Map.Entry<String, Object> e : Objects.requireNonNull(getConfigurationSection("loot")).getValues(false).entrySet()) {
                File lootFile = new File(config.getDataFolder() + File.separator + "structures" + File.separator + "loot", e.getValue().toString() + ".json");
                try {
                    loot.put(Integer.valueOf(e.getKey()), new LootTable(FileUtils.readFileToString(lootFile)));
                    Debug.info("Loaded loot table from " + lootFile.getAbsolutePath() + " with ID " + e.getKey());
                } catch(FileNotFoundException ex) {
                    Debug.stack(ex);
                    throw new NotFoundException("Loot Table File", e.getKey(), getID());
                } catch(ClassCastException | NumberFormatException ex) {
                    Debug.stack(ex);
                    throw new ConfigException("Unable to parse Structure Loot configuration! Check YAML syntax.", getID());
                } catch(ParseException parseException) {
                    Debug.stack(parseException);
                    throw new ConfigException("Invalid loot table data in file: " + lootFile.getAbsolutePath(), getID());
                }
            }
        }

        features = new ArrayList<>();
        if(contains("features")) {
            for(Map<?, ?> map : getMapList("features")) {
                for(Map.Entry<?, ?> entry : map.entrySet()) {
                    if(entry.getKey().equals("ENTITY_FEATURE"))
                        features.add(new EntityFeatureConfig((Map<String, Object>) entry.getValue()).getFeature());
                }
            }
        }

        spawn = new GridSpawn(getInt("spawn.width", 500), getInt("spawn.padding", 100));
        searchStart = new Range(getInt("spawn.start.min", 72), getInt("spawn.start.max", 72));
        bound = new Range(getInt("spawn.bound.min", 48), getInt("spawn.bound.max", 72));
    }

    @Override
    public String getID() {
        return id;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public Structure getStructure(Random r) {
        return structure.get(r);
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

    public LootTable getLoot(int id) {
        return loot.get(id);
    }
}
