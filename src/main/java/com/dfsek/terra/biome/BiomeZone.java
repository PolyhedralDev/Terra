package com.dfsek.terra.biome;

import com.dfsek.terra.config.WorldConfig;
import org.bukkit.World;
import org.polydev.gaea.biome.BiomeGrid;
import org.polydev.gaea.biome.NormalizationUtil;
import org.polydev.gaea.math.FastNoise;

import java.util.HashMap;
import java.util.Map;

public class BiomeZone {
    private BiomeGrid[] grids;
    private final World w;
    private final FastNoise noise;
    private static final Map<World, BiomeZone> zones = new HashMap<>();
    private static final double[] normalMap = new double[] {-0.572874128818512D, -0.5007192492485046D, -0.4495924413204193D, -0.41612040996551514D, -0.3814384937286377D, -0.3477869927883148D, -0.31369876861572266D, -0.28042978048324585D, -0.24612723290920258D, -0.21002958714962006D, -0.17449893057346344D, -0.1394101232290268D, -0.10480091720819473D, -0.0714595764875412D, -0.03575916960835457D, -0.0017036114586517215D, 0.03202686831355095D, 0.06717526167631149D, 0.10201185941696167D, 0.13758908212184906D, 0.17380206286907196D, 0.20863550901412964D, 0.24430148303508759D, 0.2795235514640808D, 0.31312644481658936D, 0.3475150465965271D, 0.38061848282814026D, 0.415109783411026D, 0.44838231801986694D, 0.4965132176876068D, 0.5715073347091675D, 0.7126374840736389D};

    private BiomeZone(World w, float freq) {
        this.w = w;
        this.noise = new FastNoise((int) w.getSeed()+2);
        FastNoise base = new FastNoise((int) (w.getSeed()+2));
        base.setNoiseType(FastNoise.NoiseType.Simplex);
        base.setFrequency(freq);
        this.noise.setCellularDistanceFunction(FastNoise.CellularDistanceFunction.Natural);
        this.noise.setCellularNoiseLookup(base);
        this.noise.setFrequency(freq);
        setZones(WorldConfig.fromWorld(w).definedGrids);
        zones.put(w, this);
    }

    public void setZones(BiomeGrid[] grids) {
        if(grids.length != 32) throw new IllegalArgumentException("Illegal number of grids!");
        this.grids = grids;
    }

    protected BiomeGrid getGrid(int x, int z) {
        return grids[NormalizationUtil.normalize(noise.getNoise(x, z), 32)];
    }

    protected static BiomeZone fromWorld(World w) {
        if(zones.containsKey(w)) return zones.get(w);
        else return new BiomeZone(w, WorldConfig.fromWorld(w).zoneFreq);
    }
}
