package com.dfsek.terra.addons.feature.distributor.distributors;

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
    
    @Override
    public boolean matches(int x, int z, long seed) {
        int cellX = Math.floorDiv(x, cellWidth);
        int cellZ = Math.floorDiv(z, cellWidth);
        
        Random random = new Random((MathUtil.murmur64(MathUtil.squash(cellX, cellZ)) ^ seed) + salt);
        
        int pointX = random.nextInt(width) + cellX * cellWidth;
        int pointZ = random.nextInt(width) + cellZ * cellWidth;
        
        return x == pointX && z == pointZ;
    }
}
