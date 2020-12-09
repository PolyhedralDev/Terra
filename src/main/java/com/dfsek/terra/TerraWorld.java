package com.dfsek.terra;

import com.dfsek.terra.biome.BiomeZone;
import com.dfsek.terra.biome.grid.master.TerraBiomeGrid;
import com.dfsek.terra.biome.grid.master.TerraRadialBiomeGrid;
import com.dfsek.terra.biome.grid.master.TerraStandardBiomeGrid;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.ConfigPackTemplate;
import com.dfsek.terra.config.base.WorldConfig;
import com.dfsek.terra.config.builder.biomegrid.BiomeGridBuilder;
import com.dfsek.terra.debug.Debug;
import com.dfsek.terra.generation.TerraChunkGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.polydev.gaea.biome.BiomeGrid;

import java.util.HashMap;
import java.util.Map;

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

        ConfigPackTemplate template = config.getTemplate();

        int zoneSize = template.getGrids().size();

        BiomeGrid[] definedGrids = new BiomeGrid[zoneSize];
        for(int i = 0; i < zoneSize; i++) {
            String partName = template.getGrids().get(i);
            try {
                BiomeGridBuilder g = config.getBiomeGrid(partName);
                BiomeGrid b = g.build(w, worldConfig);
                definedGrids[i] = b;
            } catch(NullPointerException e) {
                safe = false;
                Debug.stack(e);
                Bukkit.getLogger().severe("No such BiomeGrid " + partName);
                Bukkit.getLogger().severe("Please check configuration files for errors. Configuration errors will have been reported during initialization.");
                Bukkit.getLogger().severe("ONLY report this to Terra if you are SURE your config is error-free.");
                Bukkit.getLogger().severe("Terrain will NOT generate properly at this point. Correct your config before using your server!");
            }
        }
        zone = new BiomeZone(w, worldConfig, definedGrids);

        if(template.getGridType().equals(TerraBiomeGrid.Type.RADIAL)) {
            BiomeGrid internal = config.getBiomeGrid(template.getRadialInternalGrid()).build(w, worldConfig);
            grid = new TerraRadialBiomeGrid(w, template.getGridFreqX(), template.getGridFreqZ(), zone, config, template.getRadialGridRadius(), internal);
        } else grid = new TerraStandardBiomeGrid(w, template.getGridFreqX(), template.getGridFreqZ(), zone, config);
    }

    public static void loadWorld(WorldConfig w) {
        loaded.put(w.getWorldID(), w);
    }

    public static synchronized TerraWorld getWorld(World w) {
        return map.computeIfAbsent(w, TerraWorld::new);
    }

    public static synchronized void invalidate() {
        map.clear();
    }

    public static int numWorlds() {
        return map.size();
    }

    public static boolean isTerraWorld(World w) {
        return w.getGenerator() instanceof TerraChunkGenerator;
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

    public boolean isSafe() {
        return safe;
    }
}
