package com.dfsek.terra.config.genconfig;

import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.exception.ConfigException;
import org.polydev.gaea.math.Range;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.TerraConfigObject;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
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
    private Set<Material> replaceableTop;
    private Set<Material> replaceableBottom;
    private Set<Material> update;
    private Map<Material, Set<Material>> shift;
    private Map<Integer, ProbabilityCollection<BlockData>> inner;
    private Map<Integer, ProbabilityCollection<BlockData>> outer;
    private Map<Integer, ProbabilityCollection<BlockData>> top;
    private Map<Integer, ProbabilityCollection<BlockData>> bottom;
    private boolean replaceIsBlacklistInner;
    private boolean replaceIsBlacklistOuter;
    private boolean replaceIsBlacklistTop;
    private boolean replaceIsBlacklistBottom;

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
        if(!contains("id")) throw new ConfigException("No ID specified for Carver!", "null");
        id = getString("id");

        inner = getBlocks("palette.inner.blocks");

        outer = getBlocks("palette.outer.blocks");

        top = getBlocks("palette.top.blocks");

        bottom = getBlocks("palette.bottom.blocks");

        replaceableInner = ConfigUtil.toBlockData(getStringList("palette.inner.replace"), "replaceable inner", getID());

        replaceableOuter = ConfigUtil.toBlockData(getStringList("palette.outer.replace"), "replaceable outer", getID());

        replaceableTop = ConfigUtil.toBlockData(getStringList("palette.top.replace"), "replaceable top", getID());

        replaceableBottom = ConfigUtil.toBlockData(getStringList("palette.bottom.replace"), "replaceable bottom", getID());

        update = ConfigUtil.toBlockData(getStringList("update"), "update", getID());

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
        replaceIsBlacklistTop = getBoolean("palette.top.replace-blacklist", false);
        replaceIsBlacklistBottom = getBoolean("palette.bottom.replace-blacklist", false);

        double[] start = new double[] {getDouble("start.x"), getDouble("start.y"), getDouble("start.z")};
        double[] mutate = new double[] {getDouble("mutate.x"), getDouble("mutate.y"), getDouble("mutate.z"), getDouble("mutate.radius")};
        double[] radiusMultiplier = new double[] {getDouble("start.radius.multiply.x"), getDouble("start.radius.multiply.y"), getDouble("start.radius.multiply.z")};
        Range length = new Range(getInt("length.min"), getInt("length.max"));
        Range radius = new Range(getInt("start.radius.min"), getInt("start.radius.max"));
        Range height = new Range(getInt("start.height.min"), getInt("start.height.max"));

        carver = new UserDefinedCarver(height, radius, length, start, mutate, radiusMultiplier, id.hashCode(), getInt("cut.top", 0), getInt("cut.bottom", 0));
        caveConfig.put(id, this);
    }

    private Map<Integer, ProbabilityCollection<BlockData>> getBlocks(String key) throws InvalidConfigurationException {
        if(!contains(key)) throw new ConfigException("Missing Carver Palette!", getID());
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
                throw new ConfigException("Unable to parse Carver Palette configuration! Check YAML syntax:" + e.getMessage(), getID());
            }
        }
        return result;
    }

    public Map<Material, Set<Material>> getShiftedBlocks() {
        return shift;
    }

    public Set<Material> getUpdateBlocks() {
        return update;
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

    public boolean isReplaceableTop(Material m) {
        if(replaceIsBlacklistTop) {
            return !replaceableTop.contains(m);
        }
        return replaceableTop.contains(m);
    }

    public boolean isReplaceableBottom(Material m) {
        if(replaceIsBlacklistBottom) {
            return !replaceableBottom.contains(m);
        }
        return replaceableBottom.contains(m);
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

    public ProbabilityCollection<BlockData> getPaletteBottom(int y) {
        for(Map.Entry<Integer, ProbabilityCollection<BlockData>> e : bottom.entrySet()) {
            if(e.getKey() >= y ) return e.getValue();
        }
        return null;
    }

    public ProbabilityCollection<BlockData> getPaletteTop(int y) {
        for(Map.Entry<Integer, ProbabilityCollection<BlockData>> e : top.entrySet()) {
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
        return caveConfig.get(id);
    }

    public static CarverConfig fromDefinedCarver(UserDefinedCarver c) {
        for(CarverConfig co : caveConfig.values()) {
            if(co.getCarver().equals(c)) return co;
        }
        throw new IllegalArgumentException("Unable to find carver!");
    }
}
