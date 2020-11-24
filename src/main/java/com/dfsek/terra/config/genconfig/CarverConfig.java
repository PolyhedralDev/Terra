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
import java.util.EnumSet;
import java.util.HashMap;
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
        load(file);
        if(!contains("id")) throw new ConfigException("No ID specified for Carver!", "null");
        id = Objects.requireNonNull(getString("id"));

        inner = getBlocks("palette.inner.layers");

        outer = getBlocks("palette.outer.layers");

        top = getBlocks("palette.top.layers");

        bottom = getBlocks("palette.bottom.layers");

        replaceableInner = ConfigUtil.toBlockData(getStringList("palette.inner.replace"), "replaceable inner", getID());

        replaceableOuter = ConfigUtil.toBlockData(getStringList("palette.outer.replace"), "replaceable outer", getID());

        replaceableTop = ConfigUtil.toBlockData(getStringList("palette.top.replace"), "replaceable top", getID());

        replaceableBottom = ConfigUtil.toBlockData(getStringList("palette.bottom.replace"), "replaceable bottom", getID());

        update = ConfigUtil.toBlockData(getStringList("update"), "update", getID());

        updateOcean = getBoolean("update-liquids", false);

        double step = getDouble("step", 2);
        Range recalc = new Range(getInt("recalculate-direction.min", 8), getInt("recalculate-direction.max", 12));
        double rm = getDouble("recalculate-magnitude", 4);
        shift = new HashMap<>();
        for(Map.Entry<String, Object> e : Objects.requireNonNull(getConfigurationSection("shift")).getValues(false).entrySet()) {
            Set<Material> l = EnumSet.noneOf(Material.class);
            for(String s : (List<String>) e.getValue()) {
                l.add(Bukkit.createBlockData(s).getMaterial());
                Debug.info("Added " + s + " to shift-able blocks");
            }
            shift.put(Bukkit.createBlockData(e.getKey()).getMaterial(), l);
            Debug.info("Added " + e.getKey() + " as master block");
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
        carver.setStep(step);
        carver.setRecalc(recalc);
        carver.setRecalcMagnitude(rm);
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, ProbabilityCollection<BlockData>> getBlocks(String key) throws InvalidConfigurationException {
        if(!contains(key)) throw new ConfigException("Missing Carver Palette!", getID());
        Map<Integer, ProbabilityCollection<BlockData>> result = new TreeMap<>();
        for(Map<?, ?> m : getMapList(key)) {
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
