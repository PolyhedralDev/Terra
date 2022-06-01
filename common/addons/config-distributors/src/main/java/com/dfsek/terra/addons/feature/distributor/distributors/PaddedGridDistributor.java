package com.dfsek.terra.addons.feature.distributor.distributors;

import net.jafama.FastMath;

import java.util.Random;

import com.dfsek.terra.api.structure.feature.Distributor;
import com.dfsek.terra.api.util.MathUtil;


public class PaddedGridDistributor implements Distributor {
    private final int width;
    
    private final int cellWidth;
    
    private final int salt;
    
    public PaddedGridDistributor(int width, int padding, int salt) {
        this.width = width;
        this.salt = salt;
        this.cellWidth = width + padding;
    }
    
    private static long murmur64(long h) {
        h ^= h >>> 33;
        h *= 0xff51afd7ed558ccdL;
        h ^= h >>> 33;
        h *= 0xc4ceb9fe1a85ec53L;
        h ^= h >>> 33;
        return h;
    }
    
    @Override
    public boolean matches(int x, int z, long seed) {
        int cellX = FastMath.floorDiv(x, cellWidth);
        int cellZ = FastMath.floorDiv(z, cellWidth);
        
        Random random = new Random((murmur64(MathUtil.squash(cellX, cellZ)) ^ seed) + salt);
        
        int pointX = random.nextInt(width) + cellX * cellWidth;
        int pointZ = random.nextInt(width) + cellZ * cellWidth;
        
        return x == pointX && z == pointZ;
    }
}
