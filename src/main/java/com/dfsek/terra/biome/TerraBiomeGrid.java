package com.dfsek.terra.biome;

import com.dfsek.terra.config.ConfigUtil;
import com.dfsek.terra.config.WorldConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.biome.BiomeGrid;
import org.polydev.gaea.generation.GenerationPhase;

import java.util.HashMap;
import java.util.Map;

public class TerraBiomeGrid extends BiomeGrid {
    private static int failNum = 0;
    private CoordinatePerturb perturb;

    private static final Map<World, TerraBiomeGrid> grids = new HashMap<>();
    private final World w;
    private final BiomeZone zone;
    private final boolean perturbPaletteOnly;



    private TerraBiomeGrid(World w, float freq1, float freq2, boolean blank) {
        super(w, freq1, freq2);
        WorldConfig c = WorldConfig.fromWorld(w);
        if(c.biomeBlend) {
            perturb = new CoordinatePerturb(c.blendFreq, c.blendAmp, w.getSeed());
        }
        perturbPaletteOnly = c.perturbPaletteOnly;
        this.w = w;
        this.zone = BiomeZone.fromWorld(w);
        if(!blank) grids.put(w, this);
    }


    public static TerraBiomeGrid fromWorld(World w) {
        try {
            if(grids.containsKey(w)) return grids.get(w);
            else return new TerraBiomeGrid(w, WorldConfig.fromWorld(w).freq1, WorldConfig.fromWorld(w).freq2, false);
        } catch(NullPointerException e) {
            if(ConfigUtil.debug) e.printStackTrace();
            if(failNum % 256 == 0) Bukkit.getLogger().severe("[Terra] A severe configuration error has prevented Terra from properly generating terrain. Please check your configuration for errors. Any config errors will have been reported above.");
            failNum++;
            return new TerraBiomeGrid(w, 0.001f, 0.002f, true);
        }
    }

    @Override
    public Biome getBiome(int x, int z, GenerationPhase phase) {
        int xp = x;
        int zp = z;
        if(perturb != null && (!perturbPaletteOnly || phase.equals(GenerationPhase.PALETTE_APPLY))) {
            int[] perturbCoords = perturb.getShiftedCoords(x, z);
            xp = perturbCoords[0];
            zp = perturbCoords[1];
        }

        try {
            return zone.getGrid(xp, zp).getBiome(xp, zp, phase);
        } catch(NullPointerException e) {
            if(ConfigUtil.debug) e.printStackTrace();
            if(failNum % 256 == 0) Bukkit.getLogger().severe("[Terra] A severe configuration error has prevented Terra from properly generating terrain at coordinates: " + x + ", " + z + ". Please check your configuration for errors. Any config errors will have been reported above.");
            failNum++;
            return null;
        }
    }

    @Override
    public Biome getBiome(Location l, GenerationPhase phase) {
        return getBiome(l.getBlockX(), l.getBlockZ(), phase);
    }

    public static void invalidate() {
        grids.clear();
    }

    public UserDefinedGrid getGrid(int x, int z) {
        return (UserDefinedGrid) BiomeZone.fromWorld(w).getGrid(x, z);
    }
}
