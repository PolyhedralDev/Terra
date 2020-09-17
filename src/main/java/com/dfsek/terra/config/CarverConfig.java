package com.dfsek.terra.config;

import com.dfsek.terra.MaxMin;
import com.dfsek.terra.UserDefinedCarver;
import org.bukkit.Bukkit;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class CarverConfig extends YamlConfiguration {
    private static final Map<String, CarverConfig> caveConfig = new HashMap<>();
    private UserDefinedCarver carver;
    private BlockPalette inner;
    private BlockPalette walls;
    private double[] start;
    private double[] mutate;
    private double radMutate;
    private MaxMin length;
    private MaxMin radius;
    private MaxMin height;
    private String id;
    public CarverConfig(File file) throws IOException, InvalidConfigurationException {
        super();
        this.load(file);
    }


    public MaxMin getHeight() {
        return height;
    }

    public String getID() {
        return id;
    }

    public MaxMin getLength() {
        return length;
    }

    public UserDefinedCarver getCarver() {
        return carver;
    }

    @Override
    public void load(@NotNull File file) throws IOException, InvalidConfigurationException {
        super.load(file);
        if(Objects.requireNonNull(getString("palette.interior")).startsWith("BLOCK:")) {
            inner = new BlockPalette().addBlockData(new ProbabilityCollection<BlockData>().add(Bukkit.createBlockData(getString("palette.interior").substring(6)), 1), 1);
        } else {
            inner = PaletteConfig.fromID(getString("palette.interior")).getPalette();
        }

        if(Objects.requireNonNull(getString("palette.walls")).startsWith("BLOCK:")) {
            walls = new BlockPalette().addBlockData(new ProbabilityCollection<BlockData>().add(Bukkit.createBlockData(getString("palette.walls").substring(6)), 1), 1);
        } else {
            walls = PaletteConfig.fromID(getString("palette.walls")).getPalette();
        }


        start = new double[] {getDouble("start.x"), getDouble("start.y"), getDouble("start.y")};
        mutate = new double[] {getDouble("mutate.x"), getDouble("mutate.y"), getDouble("mutate.z")};
        length = new MaxMin(getInt("length.min"), getInt("length.max"));
        radius = new MaxMin(getInt("start.radius.min"), getInt("start.radius.max"));
        height = new MaxMin(getInt("start.height.min"), getInt("start.height.max"));
        radMutate = getDouble("mutate.radius");
        id = getString("id");
        carver = new UserDefinedCarver(this);
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
                        logger.info("Loading cave from " + path.toString());
                        try {
                            CarverConfig cave = new CarverConfig(path.toFile());
                            caveConfig.put(cave.getID(), cave);
                            logger.info("ID: " + cave.getID());
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
}
