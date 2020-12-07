package com.dfsek.terra.biome.grid.master;

import com.dfsek.terra.biome.BiomeZone;
import com.dfsek.terra.biome.UserDefinedBiome;
import com.dfsek.terra.biome.grid.UserDefinedGrid;
import com.dfsek.terra.biome.postprocessing.CoordinatePerturb;
import com.dfsek.terra.biome.postprocessing.ErosionNoise;
import com.dfsek.terra.config.base.ConfigPack;
import com.dfsek.terra.config.base.ConfigPackTemplate;
import com.dfsek.terra.procgen.math.Vector2;
import org.bukkit.Location;
import org.bukkit.World;
import org.polydev.gaea.biome.Biome;
import org.polydev.gaea.generation.GenerationPhase;

public class TerraStandardBiomeGrid extends TerraBiomeGrid {
    private static final int failNum = 0;
    private final BiomeZone zone;
    private CoordinatePerturb perturb;
    private ErosionNoise erode;

    public TerraStandardBiomeGrid(World w, double freq1, double freq2, BiomeZone zone, ConfigPack c) {
        super(w, freq1, freq2, 0, 0);
        ConfigPackTemplate t = c.getTemplate();
        if(c.getTemplate().isBlend()) {
            perturb = new CoordinatePerturb(t.getBlendFreq(), t.getBlendAmp(), w.getSeed());
        }
        this.zone = zone;
        if(c.getTemplate().isErode()) {
            erode = new ErosionNoise(t.getErodeFreq(), t.getErodeThresh(), t.getErodeOctaves(), w.getSeed());
        }
    }

    @Override
    public UserDefinedGrid getGrid(int x, int z) {
        return (UserDefinedGrid) zone.getGrid(x, z);
    }

    @Override
    public Biome getBiome(int x, int z, GenerationPhase phase) {
        int xp = x, zp = z;
        if(perturb != null && (phase.equals(GenerationPhase.PALETTE_APPLY) || phase.equals(GenerationPhase.POPULATE))) {
            Vector2 perturbCoords = perturb.getShiftedCoords(x, z);
            xp = (int) perturbCoords.getX();
            zp = (int) perturbCoords.getZ();
        }

        UserDefinedBiome b = (UserDefinedBiome) zone.getGrid(xp, zp).getBiome(xp, zp, phase);
        if(erode != null && erode.isEroded(xp, zp)) return b.getErode();
        return b;
    }

    @Override
    public Biome getBiome(Location l, GenerationPhase phase) {
        return getBiome(l.getBlockX(), l.getBlockZ(), phase);
    }
}
