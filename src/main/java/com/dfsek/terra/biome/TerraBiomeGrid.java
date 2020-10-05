package com.dfsek.terra.biome;

import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.biome.BiomeGrid;
import org.polydev.gaea.generation.GenerationPhase;

public class TerraBiomeGrid extends BiomeGrid {
    private static int failNum = 0;
    private CoordinatePerturb perturb;

    private final BiomeZone zone;
    private final boolean perturbPaletteOnly;

    public TerraBiomeGrid(World w, float freq1, float freq2, BiomeZone zone, ConfigPack c) {
        super(w, freq1, freq2);
        if(c.biomeBlend) {
            perturb = new CoordinatePerturb(c.blendFreq, c.blendAmp, w.getSeed());
        }
        perturbPaletteOnly = c.perturbPaletteOnly;
        this.zone = zone;
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

    public UserDefinedGrid getGrid(int x, int z) {
        return (UserDefinedGrid) zone.getGrid(x, z);
    }
}
