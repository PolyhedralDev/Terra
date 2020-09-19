package com.dfsek.terra.config;

import com.dfsek.terra.MaxMin;
import com.dfsek.terra.carving.UserDefinedCarver;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.commons.io.FilenameUtils;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.world.BlockPalette;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class CarverConfig extends YamlConfiguration {
    private static final Map<String, CarverConfig> caveConfig = new HashMap<>();
    private UserDefinedCarver carver;
    private String id;
    private final Set<Material> replaceableInner = new HashSet<>();
    private final Set<Material> replaceableOuter = new HashSet<>();
    private boolean replaceIsBlacklistInner;
    private boolean replaceIsBlacklistOuter;
    public CarverConfig(File file) throws IOException, InvalidConfigurationException {
        super();
        this.load(file);
    }


    public String getID() {
        return id;
    }

    public UserDefinedCarver getCarver() {
        return carver;
    }

    @Override
    public void load(@NotNull File file) throws IOException, InvalidConfigurationException {
        super.load(file);

        if(contains("palette.inner.replace")) {
            for(String s : getStringList("palette.inner.replace")) {
                replaceableInner.add(Bukkit.createBlockData(s).getMaterial());
            }
        }
        if(contains("palette.outer.replace")) {
            for(String s : getStringList("palette.outer.replace")) {
                replaceableOuter.add(Bukkit.createBlockData(s).getMaterial());
            }
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

    protected static void loadCaves(JavaPlugin main) {
        // TODO: Merge all load methods
        Logger logger = main.getLogger();
        caveConfig.clear();
        File oreFolder = new File(main.getDataFolder() + File.separator + "carving");
        oreFolder.mkdirs();
        try (Stream<Path> paths = Files.walk(oreFolder.toPath())) {
            paths
                    .filter(path -> FilenameUtils.wildcardMatch(path.toFile().getName(), "*.yml"))
                    .forEach(path -> {
                        try {
                            CarverConfig cave = new CarverConfig(path.toFile());
                            caveConfig.put(cave.getID(), cave);
                            logger.info("Loaded Carver with ID " + cave.getID() + " from " + path.toString());
                        } catch(IOException e) {
                            e.printStackTrace();
                        } catch(InvalidConfigurationException | IllegalArgumentException e) {
                            logger.severe("Configuration error for Carver. ");
                            logger.severe(e.getMessage());
                            logger.severe("Correct this before proceeding!");
                        }
                    });
        } catch(IOException e) {
            e.printStackTrace();
        }
        main.getLogger().info("Loaded " + caveConfig.size() + " carvers.");
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
