package com.dfsek.terra.biome;

import com.dfsek.terra.config.WorldConfig;
import org.bukkit.World;
import org.polydev.gaea.biome.BiomeGrid;
import org.polydev.gaea.math.FastNoise;

import java.util.HashMap;
import java.util.Map;

public class BiomeZone {
    private BiomeGrid[] grids;
    private final World w;
    private final FastNoise noise;
    private static final Map<World, BiomeZone> zones = new HashMap<>();
    private static final double[] normalMap = new double[] {-0.35662081837654114D, -0.30661869049072266D, -0.27095329761505127D, -0.24149227142333984D, -0.21537694334983826D, -0.19166918098926544D, -0.16956785321235657D, -0.14864568412303925D, -0.12845154106616974D, -0.10894706845283508D, -0.08996972441673279D, -0.0715663805603981D, -0.053535036742687225D, -0.03580872714519501D, -0.01817353256046772D, -7.577221258543432E-4D, 0.016616813838481903D, 0.03416096046566963D, 0.05187138542532921D, 0.06989025324583054D, 0.08827653527259827D, 0.10723070055246353D, 0.12675245106220245D, 0.14694781601428986D, 0.16793397068977356D, 0.18999846279621124D, 0.2138010412454605D, 0.24002985656261444D, 0.2696261405944824D, 0.30540621280670166D, 0.35551881790161133D, 0.653269350528717D};


    public BiomeZone(World w, float freq) {
        this.w = w;
        this.noise = new FastNoise((int) w.getSeed()+2);
        noise.setNoiseType(FastNoise.NoiseType.SimplexFractal);
        noise.setFractalOctaves(5);
        noise.setFrequency(freq);
        setZones(WorldConfig.fromWorld(w).definedGrids);
        zones.put(w, this);
    }

    public void setZones(BiomeGrid[] grids) {
        if(grids.length != 32) throw new IllegalArgumentException("Illegal number of grids!");
        this.grids = grids;
    }

    public BiomeGrid getGrid(int x, int z) {
        return grids[normalize(noise.getSimplexFractal(x, z))];
    }

    public static BiomeZone fromWorld(World w) {
        if(zones.containsKey(w)) return zones.get(w);
        else return new BiomeZone(w, WorldConfig.fromWorld(w).zoneFreq);
    }

    /**
     * Takes a noise input and normalizes it to a value between 0 and 31 inclusive.
     *
     * @param d - The noise value to normalize.
     * @return int - The normalized value.
     */
    public static int normalize(double d) {
        for(int i = 0; i < normalMap.length; i++) {
            if(d < normalMap[i]) return i;
        }
        return normalMap.length-1;
    }
}
