package com.dfsek.terra.carving;

import org.bukkit.World;
import org.bukkit.util.Vector;
import org.polydev.gaea.math.FastNoiseLite;
import org.polydev.gaea.world.carving.Carver;
import org.polydev.gaea.world.carving.CarvingData;
import org.polydev.gaea.world.carving.Worm;

import java.util.Random;

public class SimplexCarver extends Carver {
    private final FastNoiseLite noise;
    private final FastNoiseLite height;
    private final FastNoiseLite column;
    private final FastNoiseLite hasCaves;
    private final double root2inverse = 1D/Math.sqrt(2);
    public SimplexCarver(int minY, int maxY) {
        super(minY, maxY);
        noise = new FastNoiseLite(2403);
        noise.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        noise.setFractalType(FastNoiseLite.FractalType.FBm);
        noise.setFractalOctaves(3);
        noise.setFrequency(0.02f);

        height = new FastNoiseLite(2404);
        height.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        height.setFrequency(0.01f);

        column = new FastNoiseLite(2404);
        column.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        column.setFractalType(FastNoiseLite.FractalType.FBm);
        column.setFractalOctaves(5);
        column.setFrequency(0.05f);

        hasCaves = new FastNoiseLite(2405);
        hasCaves.setNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        hasCaves.setFrequency(0.005f);
    }

    @Override
    public Worm getWorm(long l, Vector vector) {
        return null;
    }

    @Override
    public boolean isChunkCarved(World world, int i, int i1, Random random) {
        return true;
    }

    @Override
    public CarvingData carve(int chunkX, int chunkZ, World w) {
        CarvingData c = new CarvingData(chunkX, chunkZ);
        int ox = chunkX << 4;
        int oz = chunkZ << 4;
        for(int x = ox; x < ox+16; x++) {
            for(int z = oz; z < oz+16; z++) {
                double heightNoise = height.getNoise(x, z);
                double mainNoise = noise.getNoise(x, z)*2;
                double columnNoise = Math.pow(Math.max(column.getNoise(x, z), 0)*2, 3);
                double hc = (acot(16*(hasCaves.getNoise(x, z)-0.2))/Math.PI)-0.1;
                CarvingData.CarvingType type = CarvingData.CarvingType.BOTTOM;
                double simplex = (Math.pow(mainNoise + root2inverse, 3)/2 + columnNoise) * hc;
                for(int y = 0; y < 64; y++) {
                    double finalNoise = (-0.05*Math.abs(y-(heightNoise*16 + 24))+1 - simplex) * hc;
                    if(finalNoise > 0.5) {
                        c.carve(x-ox, y, z-oz, type);
                        double finalNoiseUp = (-0.05*Math.abs((y+1)-(heightNoise*16 + 24))+1 - simplex) * hc;
                        if(finalNoiseUp > 0.5) {
                            type = CarvingData.CarvingType.CENTER;
                        } else type = CarvingData.CarvingType.TOP;
                    }
                }
            }
        }
        return c;
    }

    private static double acot(double x) {
        return Math.PI / 2 - Math.atan(x);
    }
}
