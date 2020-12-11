package com.dfsek.terra;

import com.dfsek.terra.api.gaea.biome.BiomeGrid;
import com.dfsek.terra.api.generic.TerraPlugin;
import com.dfsek.terra.api.generic.world.World;
import com.dfsek.terra.api.implementations.bukkit.generator.BukkitChunkGenerator;
import com.dfsek.terra.biome.BiomeZone;
import com.dfsek.terra.biome.grid.master.TerraBiomeGrid;
import com.dfsek.terra.biome.grid.master.TerraRadialBiomeGrid;
import com.dfsek.terra.biome.grid.master.TerraStandardBiomeGrid;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.ConfigPackTemplate;
import com.dfsek.terra.config.builder.biomegrid.BiomeGridBuilder;
import com.dfsek.terra.debug.Debug;

public class TerraWorld {
    private final TerraBiomeGrid grid;
    private final BiomeZone zone;
    private final ConfigPack config;
    private boolean safe;
    private final TerraProfiler profiler;


    public TerraWorld(World w, ConfigPack c, TerraPlugin main) {
        safe = true;
        config = c;
        profiler = new TerraProfiler(w);

        ConfigPackTemplate template = config.getTemplate();

        int zoneSize = template.getGrids().size();

        BiomeGrid[] definedGrids = new BiomeGrid[zoneSize];
        for(int i = 0; i < zoneSize; i++) {
            String partName = template.getGrids().get(i);
            try {
                BiomeGridBuilder g = config.getBiomeGrid(partName);
                BiomeGrid b = g.build(w, c);
                definedGrids[i] = b;
            } catch(NullPointerException e) {
                safe = false;
                Debug.stack(e);
                main.getLogger().severe("No such BiomeGrid " + partName);
                main.getLogger().severe("Please check configuration files for errors. Configuration errors will have been reported during initialization.");
                main.getLogger().severe("ONLY report this to Terra if you are SURE your config is error-free.");
                main.getLogger().severe("Terrain will NOT generate properly at this point. Correct your config before using your server!");
            }
        }
        zone = new BiomeZone(w, c, definedGrids);

        if(template.getGridType().equals(TerraBiomeGrid.Type.RADIAL)) {
            BiomeGrid internal = config.getBiomeGrid(template.getRadialInternalGrid()).build(w, c);
            grid = new TerraRadialBiomeGrid(w, template.getGridFreqX(), template.getGridFreqZ(), zone, config, template.getRadialGridRadius(), internal);
        } else grid = new TerraStandardBiomeGrid(w, template.getGridFreqX(), template.getGridFreqZ(), zone, config);
    }

    public static boolean isTerraWorld(World w) {
        return w.getGenerator() instanceof BukkitChunkGenerator;
    }

    public TerraBiomeGrid getGrid() {
        return grid;
    }

    public ConfigPack getConfig() {
        return config;
    }

    public BiomeZone getZone() {
        return zone;
    }

    public boolean isSafe() {
        return safe;
    }

    public TerraProfiler getProfiler() {
        return profiler;
    }
}
