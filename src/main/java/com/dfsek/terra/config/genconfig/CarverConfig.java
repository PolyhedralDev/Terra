package com.dfsek.terra.config.genconfig;

import com.dfsek.terra.Debug;
import com.dfsek.terra.carving.UserDefinedCarver;
import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.exception.ConfigException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.math.Range;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public class CarverConfig extends TerraConfig {
    private final UserDefinedCarver carver;
    private final String id;
    private final Set<Material> replaceableInner;
    private final Set<Material> replaceableOuter;
    private final Set<Material> replaceableTop;
    private final Set<Material> replaceableBottom;
    private final Set<Material> update;
    private final Map<Material, Set<Material>> shift;
    private final Map<Integer, ProbabilityCollection<BlockData>> inner;
    private final Map<Integer, ProbabilityCollection<BlockData>> outer;
    private final Map<Integer, ProbabilityCollection<BlockData>> top;
    private final Map<Integer, ProbabilityCollection<BlockData>> bottom;
    private final boolean replaceIsBlacklistInner;
    private final boolean replaceIsBlacklistOuter;
    private final boolean replaceIsBlacklistTop;
    private final boolean replaceIsBlacklistBottom;
    private final boolean updateOcean;

    @SuppressWarnings("unchecked")
    public CarverConfig(File file, ConfigPack config) throws IOException, InvalidConfigurationException {
        super(file, config);
        if(!yaml.contains("id")) throw new ConfigException("No ID specified for Carver!", "null");
        id = Objects.requireNonNull(yaml.getString("id"));

        inner = getBlocks("palette.inner.layers");

        outer = getBlocks("palette.outer.layers");

        top = getBlocks("palette.top.layers");

        bottom = getBlocks("palette.bottom.layers");

        replaceableInner = ConfigUtil.toBlockData(yaml.getStringList("palette.inner.replace"), "replaceable inner", getID());

        replaceableOuter = ConfigUtil.toBlockData(yaml.getStringList("palette.outer.replace"), "replaceable outer", getID());

        replaceableTop = ConfigUtil.toBlockData(yaml.getStringList("palette.top.replace"), "replaceable top", getID());

        replaceableBottom = ConfigUtil.toBlockData(yaml.getStringList("palette.bottom.replace"), "replaceable bottom", getID());

        update = ConfigUtil.toBlockData(yaml.getStringList("update"), "update", getID());

        updateOcean = yaml.getBoolean("update-liquids", false);

        double step = yaml.getDouble("step", 2);
        Range recalc = new Range(yaml.getInt("recalculate-direction.min", 8), yaml.getInt("recalculate-direction.max", 12));
        double rm = yaml.getDouble("recalculate-magnitude", 4);
        shift = new HashMap<>();
        for(Map.Entry<String, Object> e : Objects.requireNonNull(yaml.getConfigurationSection("shift")).getValues(false).entrySet()) {
            Set<Material> l = new HashSet<>();
            for(String s : (List<String>) e.getValue()) {
                l.add(Bukkit.createBlockData(s).getMaterial());
                Debug.info("Added " + s + " to shift-able blocks");
            }
            shift.put(Bukkit.createBlockData(e.getKey()).getMaterial(), l);
            Debug.info("Added " + e.getKey() + " as master block");
        }

        replaceIsBlacklistInner = yaml.getBoolean("palette.inner.replace-blacklist", false);
        replaceIsBlacklistOuter = yaml.getBoolean("palette.outer.replace-blacklist", false);
        replaceIsBlacklistTop = yaml.getBoolean("palette.top.replace-blacklist", false);
        replaceIsBlacklistBottom = yaml.getBoolean("palette.bottom.replace-blacklist", false);

        double[] start = new double[] {yaml.getDouble("start.x"), yaml.getDouble("start.y"), yaml.getDouble("start.z")};
        double[] mutate = new double[] {yaml.getDouble("mutate.x"), yaml.getDouble("mutate.y"), yaml.getDouble("mutate.z"), yaml.getDouble("mutate.radius")};
        double[] radiusMultiplier = new double[] {yaml.getDouble("start.radius.multiply.x"), yaml.getDouble("start.radius.multiply.y"), yaml.getDouble("start.radius.multiply.z")};
        Range length = new Range(yaml.getInt("length.min"), yaml.getInt("length.max"));
        Range radius = new Range(yaml.getInt("start.radius.min"), yaml.getInt("start.radius.max"));
        Range height = new Range(yaml.getInt("start.height.min"), yaml.getInt("start.height.max"));

        carver = new UserDefinedCarver(height, radius, length, start, mutate, radiusMultiplier, id.hashCode(), yaml.getInt("cut.top", 0), yaml.getInt("cut.bottom", 0));
        carver.setStep(step);
        carver.setRecalc(recalc);
        carver.setRecalcMagnitude(rm);
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, ProbabilityCollection<BlockData>> getBlocks(String key) throws InvalidConfigurationException {
        if(!yaml.contains(key)) throw new ConfigException("Missing Carver Palette!", getID());
        Map<Integer, ProbabilityCollection<BlockData>> result = new TreeMap<>();
        for(Map<?, ?> m : yaml.getMapList(key)) {
            try {
                ProbabilityCollection<BlockData> layer = new ProbabilityCollection<>();
                for(Map.Entry<String, Integer> type : ((Map<String, Integer>) m.get("materials")).entrySet()) {
                    layer.add(Bukkit.createBlockData(type.getKey()), type.getValue());
                    Debug.info("Added " + type.getKey() + " with probability " + type.getValue());
                }
                result.put((Integer) m.get("y"), layer);
                Debug.info("Added at level " + m.get("y"));
            } catch(ClassCastException e) {
                throw new ConfigException("Unable to parse Carver Palette configuration! Check YAML syntax:" + e.getMessage(), getID());
            }
        }
        return result;
    }

    public String getID() {
        return id;
    }

    public UserDefinedCarver getCarver() {
        return carver;
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
            if(e.getKey() >= y) return e.getValue();
        }
        return null;
    }

    public ProbabilityCollection<BlockData> getPaletteOuter(int y) {
        for(Map.Entry<Integer, ProbabilityCollection<BlockData>> e : outer.entrySet()) {
            if(e.getKey() >= y) return e.getValue();
        }
        return null;
    }

    public ProbabilityCollection<BlockData> getPaletteBottom(int y) {
        for(Map.Entry<Integer, ProbabilityCollection<BlockData>> e : bottom.entrySet()) {
            if(e.getKey() >= y) return e.getValue();
        }
        return null;
    }

    public ProbabilityCollection<BlockData> getPaletteTop(int y) {
        for(Map.Entry<Integer, ProbabilityCollection<BlockData>> e : top.entrySet()) {
            if(e.getKey() >= y) return e.getValue();
        }
        return null;
    }

    public boolean shouldUpdateOcean() {
        return updateOcean;
    }

    @Override
    public String toString() {
        return "Carver with ID " + getID();
    }
}
