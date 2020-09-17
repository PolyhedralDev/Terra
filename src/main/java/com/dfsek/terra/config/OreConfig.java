package com.dfsek.terra.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.commons.io.FilenameUtils;
import org.polydev.gaea.math.FastNoise;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class OreConfig extends YamlConfiguration {
    private static final Map<String, OreConfig> ores = new HashMap<>();
    private BlockData oreData;
    private int min;
    private int max;
    private double deform;
    private double deformFrequency;
    private String id;
    private String friendlyName;
    private int h;
    List<Material> replaceable;
    public OreConfig(File file) throws IOException, InvalidConfigurationException {
        this.load(file);
    }

    @Override
    public void load(@NotNull File file) throws IOException, InvalidConfigurationException {
        super.load(file);
        if(!contains("material")) throw new InvalidConfigurationException("Ore material not found!");
        if(!contains("deform")) throw new InvalidConfigurationException("Ore vein deformation not found!");
        if(!contains("id")) throw new InvalidConfigurationException("Ore ID not found!");
        if(!contains("replace")) throw new InvalidConfigurationException("Ore replaceable materials not found!");
        min = getInt("radius.min", 1);
        max = getInt("radius.max", 1);
        h = 2;
        deform = getDouble("deform");
        deformFrequency = getDouble("deform-frequency");
        this.id = getString("id");

        try {
            replaceable = ConfigUtil.getElements(getStringList("replace"), Material.class);
            Bukkit.getLogger().info("[Terra] Added " + replaceable.size() + " items to replaceable list.");
        } catch(IllegalArgumentException e) {
            throw new InvalidConfigurationException("Invalid material ID in replace list: " + e.getMessage());
        }

        try {
            oreData = Bukkit.createBlockData(Objects.requireNonNull(getString("material")));
        } catch(NullPointerException | IllegalArgumentException e) {
            throw new InvalidConfigurationException("Invalid ore material: " + getString("material"));
        }
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
                        if(replaceable.contains(b.getType()) && b.getLocation().getY() >= 0) b.setBlockData(oreData);
                    }
                }
            }
        }
    }

    public String getID() {
        return id;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    protected static void loadOres(JavaPlugin main) {
        // TODO: Merge all load methods
        Logger logger = main.getLogger();
        ores.clear();
        File oreFolder = new File(main.getDataFolder() + File.separator + "ores");
        oreFolder.mkdirs();
        try (Stream<Path> paths = Files.walk(oreFolder.toPath())) {
            paths
                    .filter(path -> FilenameUtils.wildcardMatch(path.toFile().getName(), "*.yml"))
                    .forEach(path -> {
                        logger.info("Loading ore from " + path.toString());
                        try {
                            OreConfig ore = new OreConfig(path.toFile());
                            ores.put(ore.getID(), ore);
                            logger.info("ID: " + ore.getID());
                        } catch(IOException e) {
                            e.printStackTrace();
                        } catch(InvalidConfigurationException | IllegalArgumentException e) {
                            logger.severe("Configuration error for Ore. ");
                            logger.severe(e.getMessage());
                            logger.severe("Correct this before proceeding!");
                        }
                    });
        } catch(IOException e) {
            e.printStackTrace();
        }
        main.getLogger().info("Loaded " + ores.size() + " ores.");
    }
    public static OreConfig fromID(String id) {
        return ores.get(id);
    }
}
