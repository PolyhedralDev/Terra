package com.dfsek.terra.biome;

import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.procgen.math.Vector2;
import org.bukkit.Location;
import org.bukkit.World;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.biome.BiomeGrid;
import org.polydev.gaea.generation.GenerationPhase;

import java.util.logging.Level;

public class TerraBiomeGrid extends BiomeGrid {
    private static int failNum = 0;
    private CoordinatePerturb perturb;
    private ErosionNoise erode;
    private UserDefinedGrid erosionGrid;

    private final BiomeZone zone;

    public TerraBiomeGrid(World w, float freq1, float freq2, BiomeZone zone, ConfigPack c, UserDefinedGrid erosion) {
        super(w, freq1, freq2, 0, 0);
        if(c.biomeBlend) {
            perturb = new CoordinatePerturb(c.blendFreq, c.blendAmp, w.getSeed());
        }
        this.zone = zone;
        if(c.erosionEnable) {
            erode = new ErosionNoise(c.erosionFreq, c.erosionThresh, c.erosionOctaves, w.getSeed());
            this.erosionGrid = erosion;
        }
    }

    @Override
    public Biome getBiome(int x, int z, GenerationPhase phase) {
        int xp = x;
        int zp = z;
        if(perturb != null && phase.equals(GenerationPhase.PALETTE_APPLY)) {
            Vector2 perturbCoords = perturb.getShiftedCoords(x, z);
            xp = (int) perturbCoords.getX();
            zp = (int) perturbCoords.getZ();
        }

        UserDefinedBiome b;
        try {
            b = (UserDefinedBiome) zone.getGrid(xp, zp).getBiome(xp, zp, phase);
        } catch(NullPointerException e) {
            if(ConfigUtil.debug) e.printStackTrace();
            if(failNum % 256 == 0) LangUtil.log("error.severe-config", Level.SEVERE, String.valueOf(x), String.valueOf(z));
            failNum++;
            return ConfigUtil.failType.fail();
        }
        if(erode != null && b.isErodible() && erode.isEroded(xp, zp)) {
            return erosionGrid.getBiome(xp, zp, phase);
        }
        return b;
    }

    @Override
    public Biome getBiome(Location l, GenerationPhase phase) {
        return getBiome(l.getBlockX(), l.getBlockZ(), phase);
    }

    public UserDefinedGrid getGrid(int x, int z) {
        return (UserDefinedGrid) zone.getGrid(x, z);
    }
}
