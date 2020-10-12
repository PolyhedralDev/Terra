package com.dfsek.terra;

import com.dfsek.terra.biome.BiomeZone;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.UserDefinedGrid;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.base.WorldConfig;
import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.config.genconfig.BiomeGridConfig;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TerraWorld {
    private static final Map<World, TerraWorld> map = new HashMap<>();
    private static final Map<String, WorldConfig> loaded = new HashMap<>();
    private final TerraBiomeGrid grid;
    private final BiomeZone zone;
    private final ConfigPack config;
    private final WorldConfig worldConfig;
    private boolean safe;

    private TerraWorld(World w) {
        safe = true;
        worldConfig = loaded.get(w.getName());
        config = worldConfig.getConfig();
        UserDefinedGrid[] definedGrids = new UserDefinedGrid[config.biomeList.size()];
        for(int i = 0; i < config.biomeList.size(); i++) {
            String partName = config.biomeList.get(i);
            try {
                if(partName.startsWith("BIOME:")) {
                    UserDefinedBiome[][] temp = new UserDefinedBiome[1][1];
                    UserDefinedBiome b = config.getBiomes().get(partName.substring(6)).getBiome();
                    temp[0][0] = b;
                    definedGrids[i] = new UserDefinedGrid(w, config.freq1, config.freq2, temp, worldConfig);
                    Debug.info("Loaded single-biome grid " + partName);
                } else {
                    BiomeGridConfig g = config.getBiomeGrid(partName);
                    Debug.info("Loaded BiomeGrid " + g.getID());
                    definedGrids[i] = g.getGrid(w, worldConfig);
                }
            } catch(NullPointerException e) {
                safe = false;
                Debug.stack(e);
                Bukkit.getLogger().severe("No such BiomeGrid " + partName);
                Bukkit.getLogger().severe("Please check configuration files for errors. Configuration errors will have been reported during initialization.");
                Bukkit.getLogger().severe("ONLY report this to Terra if you are SURE your config is error-free.");
                Bukkit.getLogger().severe("Terrain will NOT generate properly at this point. Correct your config before using your server!");
            }
        }
        UserDefinedGrid erosion = null;
        if(config.erosionEnable) {
            try {
                if(config.erosionName.startsWith("BIOME:")) {
                    UserDefinedBiome[][] temp = new UserDefinedBiome[1][1];
                    UserDefinedBiome b = Objects.requireNonNull(config.getBiome(config.erosionName.substring(6)).getBiome());
                    temp[0][0] = b;
                    erosion = new UserDefinedGrid(w, config.freq1, config.freq2, temp, worldConfig);
                    Debug.info("Loaded single-biome erosion grid " + config.erosionName);
                } else {
                    BiomeGridConfig g = Objects.requireNonNull(config.getBiomeGrid(config.erosionName));
                    Debug.info("Loaded BiomeGrid " + g.getID());
                    erosion = g.getGrid(w, worldConfig);
                }
            } catch(NullPointerException e) {
                safe = false;
                Debug.stack(e);
                Bukkit.getLogger().severe("No such BiomeGrid (erosion): " + config.erosionName);
                Bukkit.getLogger().severe("Please check configuration files for errors. Configuration errors will have been reported during initialization.");
                Bukkit.getLogger().severe("ONLY report this to Terra if you are SURE your config is error-free.");
                Bukkit.getLogger().severe("Terrain will NOT generate properly at this point. Correct your config before using your server!");
            }
        }
        zone = new BiomeZone(w, worldConfig, definedGrids);
        grid = new TerraBiomeGrid(w, config.freq1, config.freq2, zone, config, erosion);
    }

    public static void loadWorld(WorldConfig w) {
        loaded.put(w.getWorldID(), w);
    }

    public static synchronized TerraWorld getWorld(World w) {
        return map.computeIfAbsent(w, TerraWorld::new);
    }

    public TerraBiomeGrid getGrid() {
        return grid;
    }

    public ConfigPack getConfig() {
        return config;
    }

    public WorldConfig getWorldConfig() {
        return worldConfig;
    }

    public BiomeZone getZone() {
        return zone;
    }

    public static synchronized void invalidate() {
        map.clear();
        for(WorldConfig config : loaded.values()) {
            config.load(); // Reload all stored WorldConfigs
        }
    }

    public static int numWorlds() {
        return map.size();
    }

    public boolean isSafe() {
        return safe;
    }
}
