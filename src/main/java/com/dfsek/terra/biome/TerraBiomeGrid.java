package com.dfsek.terra.biome;

import com.dfsek.terra.config.ConfigUtil;
import com.dfsek.terra.config.WorldConfig;
import org.bukkit.Location;
import org.bukkit.World;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.biome.BiomeGrid;

import java.util.HashMap;
import java.util.Map;

public class TerraBiomeGrid extends BiomeGrid {

    private static final Map<World, TerraBiomeGrid> grids = new HashMap<>();
    private final World w;

    public TerraBiomeGrid(World w) {
        super(w, 1f/256, 1f/512);
        this.w = w;
        grids.put(w, this);
    }


    public static TerraBiomeGrid fromWorld(World w) {
        if(grids.containsKey(w)) return grids.get(w);
        else return new TerraBiomeGrid(w);
    }

    @Override
    public Biome getBiome(int x, int z) {
        return BiomeZone.fromWorld(w).getGrid(x, z).getBiome(x, z);
    }

    @Override
    public Biome getBiome(Location l) {
        return getBiome(l.getBlockX(), l.getBlockZ());
    }
}
