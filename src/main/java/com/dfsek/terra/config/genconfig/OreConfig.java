package com.dfsek.terra.config.genconfig;

import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.TerraConfigObject;
import com.dfsek.terra.config.exception.ConfigException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.FastNoise;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class OreConfig extends TerraConfigObject {
    private static final Map<String, OreConfig> ores = new HashMap<>();
    private BlockData oreData;
    private int min;
    private int max;
    private double deform;
    private double deformFrequency;
    private String id;
    Set<Material> replaceable;
    public OreConfig(File file) throws IOException, InvalidConfigurationException {
        super(file);
    }

    @Override
    public void init() throws InvalidConfigurationException {
        if(!contains("id")) throw new ConfigException("Ore ID not found!", "null");
        this.id = getString("id");
        if(!contains("material")) throw new ConfigException("Ore material not found!", getID());
        if(!contains("deform")) throw new ConfigException("Ore vein deformation not found!", getID());
        if(!contains("replace")) throw new ConfigException("Ore replaceable materials not found!", getID());
        min = getInt("radius.min", 1);
        max = getInt("radius.max", 1);
        deform = getDouble("deform");
        deformFrequency = getDouble("deform-frequency");

        replaceable = ConfigUtil.toBlockData(getStringList("replace"), "replaceable", getID());

        try {
            oreData = Bukkit.createBlockData(Objects.requireNonNull(getString("material")));
        } catch(NullPointerException | IllegalArgumentException e) {
            throw new ConfigException("Invalid ore material: " + getString("material"), getID());
        }
        ores.put(id, this);
    }
    private int randomInRange(Random r) {
        return r.nextInt(max-min+1)+min;
    }
    public void doVein(Location l, Random r) {
        FastNoise ore = new FastNoise(r.nextInt());
        ore.setNoiseType(FastNoise.NoiseType.SimplexFractal);
        ore.setFrequency((float) deformFrequency);
        int rad = randomInRange(r);
        for(int x = -rad; x <= rad; x++) {
            for(int y = -rad; y <= rad; y++) {
                for(int z = -rad; z <= rad; z++) {
                    if(l.clone().add(x, y, z).distance(l) < (rad + 0.5) * ((ore.getSimplexFractal(x, y, z)+1)*deform)) {
                        Block b = l.clone().add(x, y, z).getBlock();
                        if(replaceable.contains(b.getType()) && b.getLocation().getY() >= 0) b.setBlockData(oreData, false);
                    }
                }
            }
        }
    }

    public static List<String> getOreIDs() {
        return new ArrayList<>(ores.keySet());
    }

    @Override
    public String toString() {
        return "Ore with ID " + getID();
    }

    public String getID() {
        return id;
    }

    public static OreConfig fromID(String id) {
        return ores.get(id);
    }
}
