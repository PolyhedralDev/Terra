package com.dfsek.terra.biome;

import com.dfsek.terra.config.ConfigUtil;
import com.dfsek.terra.config.WorldConfig;
import org.bukkit.World;
import org.polydev.gaea.biome.BiomeGrid;

import java.util.HashMap;
import java.util.Map;

public class TerraBiomeGrid extends BiomeGrid {

    private static final Map<World, TerraBiomeGrid> grids = new HashMap<>();
    private final UserDefinedBiome[][] grid;
    private final World w;

    public TerraBiomeGrid(World w) {
        super(w, 1f/256, 1f/512);
        this.w = w;
        grid = new UserDefinedBiome[16][16];
        load();
        grids.put(w, this);
    }

    public void load() {
        super.setGrid(WorldConfig.fromWorld(w).biomeGrid.getBiomeGrid());
    }

    public static TerraBiomeGrid fromWorld(World w) {
        if(grids.containsKey(w)) return grids.get(w);
        else return new TerraBiomeGrid(w);
    }
    public static void reloadAll() {
        for(Map.Entry<World, TerraBiomeGrid> e : grids.entrySet()) {
            e.getValue().load();
        }
    }

}
