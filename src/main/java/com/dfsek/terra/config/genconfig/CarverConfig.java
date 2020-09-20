package com.dfsek.terra.config.genconfig;

import com.dfsek.terra.MaxMin;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.ConfigLoader;
import com.dfsek.terra.config.TerraConfigObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.polydev.gaea.math.ProbabilityCollection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class CarverConfig extends TerraConfigObject {
    private static final Map<String, CarverConfig> caveConfig = new HashMap<>();
    private UserDefinedCarver carver;
    private String id;
    private Set<Material> replaceableInner;
    private Set<Material> replaceableOuter;
    private Map<Material, Set<Material>> shift;
    private Map<Integer, ProbabilityCollection<BlockData>> inner;
    private Map<Integer, ProbabilityCollection<BlockData>> outer;
    private boolean replaceIsBlacklistInner;
    private boolean replaceIsBlacklistOuter;

    public CarverConfig(File file) throws IOException, InvalidConfigurationException {
        super(file);
    }

    public String getID() {
        return id;
    }

    public UserDefinedCarver getCarver() {
        return carver;
    }

    @Override
    public void init() throws InvalidConfigurationException {
        inner = getBlocks("palette.inner.blocks");

        outer = getBlocks("palette.outer.blocks");

        replaceableInner = new HashSet<>();
        replaceableOuter = new HashSet<>();

        for(String s : getStringList("palette.inner.replace")) {
            try {
                if(replaceableInner.contains(Bukkit.createBlockData(s).getMaterial())) Bukkit.getLogger().warning("Duplicate material in replaceable list: " + s);
                replaceableInner.add(Bukkit.createBlockData(s).getMaterial());
            } catch(NullPointerException | IllegalArgumentException e) {
                throw new InvalidConfigurationException("Could not load data for " + s);
            }
        }
        for(String s : getStringList("palette.outer.replace")) {
            try {
                if(replaceableOuter.contains(Bukkit.createBlockData(s).getMaterial())) Bukkit.getLogger().warning("Duplicate material in replaceable list: " + s);
                replaceableOuter.add(Bukkit.createBlockData(s).getMaterial());
            } catch(NullPointerException | IllegalArgumentException e) {
                throw new InvalidConfigurationException("Could not load data for " + s);
            }
        }
        shift = new HashMap<>();
        for(Map.Entry<String, Object> e : getConfigurationSection("shift").getValues(false).entrySet()) {
            Set<Material> l = new HashSet<>();
            for(String s : (List<String>) e.getValue()) {
                l.add(Bukkit.createBlockData(s).getMaterial());
                Bukkit.getLogger().info("Added " + s + " to shift-able blocks");
            }
            shift.put(Bukkit.createBlockData(e.getKey()).getMaterial(), l);
            Bukkit.getLogger().info("Added " + e.getKey() + " as master block");
        }

        replaceIsBlacklistInner = getBoolean("palette.inner.replace-blacklist", false);
        replaceIsBlacklistOuter = getBoolean("palette.outer.replace-blacklist", false);

        double[] start = new double[] {getDouble("start.x"), getDouble("start.y"), getDouble("start.z")};
        double[] mutate = new double[] {getDouble("mutate.x"), getDouble("mutate.y"), getDouble("mutate.z"), getDouble("mutate.radius")};
        double[] radiusMultiplier = new double[] {getDouble("start.radius.multiply.x"), getDouble("start.radius.multiply.y"), getDouble("start.radius.multiply.z")};
        MaxMin length = new MaxMin(getInt("length.min"), getInt("length.max"));
        MaxMin radius = new MaxMin(getInt("start.radius.min"), getInt("start.radius.max"));
        MaxMin height = new MaxMin(getInt("start.height.min"), getInt("start.height.max"));
        id = getString("id");
        carver = new UserDefinedCarver(height, radius, length, start, mutate, radiusMultiplier);
        caveConfig.put(id, this);
    }

    private Map<Integer, ProbabilityCollection<BlockData>> getBlocks(String key) throws InvalidConfigurationException {
        if(!contains(key)) throw new InvalidConfigurationException("Missing Carver Palette!");
        Map<Integer, ProbabilityCollection<BlockData>> result = new TreeMap<>();
        for(Map<?, ?> m : getMapList(key)) {
            try {
                ProbabilityCollection<BlockData> layer = new ProbabilityCollection<>();
                for(Map.Entry<String, Integer> type : ((Map<String, Integer>) m.get("materials")).entrySet()) {
                    layer.add(Bukkit.createBlockData(type.getKey()), type.getValue());
                    Bukkit.getLogger().info("Added " + type.getKey() + " with probability " + type.getValue());
                }
                result.put((Integer) m.get("y"), layer);
                Bukkit.getLogger().info("Added at level " + m.get("y"));
            } catch(ClassCastException e) {
                throw new InvalidConfigurationException("SEVERE configuration error for Carver Palette: \n\n" + e.getMessage());
            }
        }
        return result;
    }

    public Map<Material, Set<Material>> getShiftedBlocks() {
        return shift;
    }

    public boolean isReplaceableInner(Material m) {
        if(replaceIsBlacklistInner) {
            return !replaceableInner.contains(m);
        }
        return replaceableInner.contains(m);
    }

    public boolean isReplaceableOuter(Material m) {
        if(replaceIsBlacklistOuter) {
            return !replaceableOuter.contains(m);
        }
        return replaceableOuter.contains(m);
    }

    public ProbabilityCollection<BlockData> getPaletteInner(int y) {
        for(Map.Entry<Integer, ProbabilityCollection<BlockData>> e : inner.entrySet()) {
            if(e.getKey() >= y ) return e.getValue();
        }
        return null;
    }

    public ProbabilityCollection<BlockData> getPaletteOuter(int y) {
        for(Map.Entry<Integer, ProbabilityCollection<BlockData>> e : outer.entrySet()) {
            if(e.getKey() >= y ) return e.getValue();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Carver with ID " + getID();
    }

    public static List<CarverConfig> getCarvers() {
        return new ArrayList<>(caveConfig.values());
    }
    public static CarverConfig fromID(String id) {
        Bukkit.getLogger().info("Accessing carvers...");
        for(Map.Entry<String, CarverConfig> e : caveConfig.entrySet()) {
            Bukkit.getLogger().info("Carver ID " + e.getKey() + ", carver: " + e.getValue());
        }
        Bukkit.getLogger().info("ID requested: " + id);
        Bukkit.getLogger().info("Fetched " + caveConfig.get(id));
        return caveConfig.get(id);
    }

    public static CarverConfig fromDefinedCarver(UserDefinedCarver c) {
        for(CarverConfig co : caveConfig.values()) {
            if(co.getCarver().equals(c)) return co;
        }
        throw new IllegalArgumentException("Unable to find carver!");
    }
}
