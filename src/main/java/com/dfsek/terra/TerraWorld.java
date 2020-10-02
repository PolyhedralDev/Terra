package com.dfsek.terra;

import com.dfsek.terra.biome.BiomeZone;
import com.dfsek.terra.biome.TerraBiomeGrid;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.UserDefinedGrid;
import com.dfsek.terra.config.TerraConfig;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.base.WorldConfig;
import com.dfsek.terra.config.exception.NotFoundException;
import com.dfsek.terra.config.genconfig.BiomeGridConfig;
import com.dfsek.terra.generation.TerraChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

public class TerraWorld {
    private static final Map<World, TerraWorld> map = new HashMap<>();
    private final TerraBiomeGrid grid;
    private final BiomeZone zone;
    private final TerraConfig config;
    private final WorldConfig worldConfig;
    private TerraWorld(World w) {
        worldConfig = new WorldConfig(w, Terra.getInstance());
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
                    Terra.getInstance().getLogger().info("Loaded single-biome grid " + partName);
                } else {
                    BiomeGridConfig g = config.getBiomeGrid(partName);
                    Debug.info(g.getID());
                    definedGrids[i] = g.getGrid(w, worldConfig);
                }
            } catch(NullPointerException e) {
                if(ConfigUtil.debug) e.printStackTrace();
                Bukkit.getLogger().severe("No suck BiomeGrid " + partName);
            }
        }
        zone = new BiomeZone(w, worldConfig, definedGrids);
        grid = new TerraBiomeGrid(w, config.freq1, config.freq2, zone, config);
    }

    public static TerraWorld getWorld(World w) {
        return map.computeIfAbsent(w, TerraWorld::new);
    }

    public TerraBiomeGrid getGrid() {
        return grid;
    }

    public TerraConfig getConfig() {
        return config;
    }

    public WorldConfig getWorldConfig() {
        return worldConfig;
    }

    public BiomeZone getZone() {
        return zone;
    }

    public static void invalidate() {
        map.clear();
    }
}
