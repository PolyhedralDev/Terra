package com.dfsek.terra.config;

import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.UserDefinedGrid;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class BiomeGridConfig extends YamlConfiguration {
    private static final Map<String, BiomeGridConfig> biomeGrids = new HashMap<>();
    private String gridID;
    private String friendlyName;
    private boolean isEnabled = false;
    private final UserDefinedBiome[][] gridRaw = new UserDefinedBiome[16][16];

    public BiomeGridConfig(File file) throws IOException, InvalidConfigurationException {
        super();
        load(file);
    }
    @Override
    public void load(@NotNull File file) throws IOException, InvalidConfigurationException {
        isEnabled = false;
        super.load(file);
        if(!contains("id")) throw new InvalidConfigurationException("Grid ID unspecified!");
        this.gridID = getString("id");
        if(!contains("name")) throw new InvalidConfigurationException("Grid Name unspecified!");
        this.friendlyName = getString("name");
        if(!contains("grid")) throw new InvalidConfigurationException("Grid not found!");
        try {
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    try {
                        gridRaw[x][z] = BiomeConfig.fromID(((List<List<String>>) getList("grid")).get(x).get(z)).getBiome();
                    } catch(NullPointerException e) {
                        throw new InvalidConfigurationException("SEVERE configuration error for BiomeGrid " + getFriendlyName() + ", ID: " + getGridID() + "\n\nNo such biome " + ((List<List<String>>) getList("grid")).get(x).get(z));
                    }
                }
            }
        } catch(ClassCastException e) {
            throw new InvalidConfigurationException("Malformed grid!");
        }
        isEnabled = true;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public UserDefinedBiome[][] getBiomeGrid() {
        return gridRaw;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public String getGridID() {
        return gridID;
    }

    public UserDefinedGrid getGrid(World w) {
        WorldConfig c = WorldConfig.fromWorld(w);
        return new UserDefinedGrid(w, c.freq1, c.freq2, this);
    }

    protected static void loadBiomeGrids(JavaPlugin main) {
        File biomeGridFolder = new File(main.getDataFolder() + File.separator + "grids");
        biomeGridFolder.mkdirs();
        try (Stream<Path> paths = Files.walk(biomeGridFolder.toPath())) {
            paths
                    .filter(path -> FilenameUtils.wildcardMatch(path.toFile().getName(), "*.yml"))
                    .forEach(path -> {
                        try {
                            BiomeGridConfig grid = new BiomeGridConfig(path.toFile());
                            biomeGrids.put(grid.getGridID(), grid);
                            main.getLogger().info("Loaded BiomeGrid with name " + grid.getFriendlyName() + ", ID " + grid.getGridID() + " from " + path.toString());
                        } catch(IOException e) {
                            e.printStackTrace();
                        } catch(InvalidConfigurationException | IllegalArgumentException e) {
                            Bukkit.getLogger().severe("[Terra] Configuration error for BiomeGrid. ");
                            Bukkit.getLogger().severe("[Terra] " + e.getMessage());
                            Bukkit.getLogger().severe("[Terra] Correct this before proceeding!");
                        }
                    });
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, BiomeGridConfig> getBiomeGrids() {
        return biomeGrids;
    }
}
