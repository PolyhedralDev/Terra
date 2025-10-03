package com.dfsek.terra.addons.feature.distributor.distributors;

import com.dfsek.seismic.algorithms.hashing.HashingFunctions;
import com.dfsek.seismic.math.integer.IntegerFunctions;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

import com.dfsek.terra.api.structure.feature.Distributor;


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

        RandomGenerator random = RandomGeneratorFactory.<RandomGenerator.SplittableGenerator>of("Xoroshiro128PlusPlus").create(
            (HashingFunctions.murmur64(IntegerFunctions.squash(cellX, cellZ)) ^ seed) + salt);

        int pointX = random.nextInt(width) + cellX * cellWidth;
        int pointZ = random.nextInt(width) + cellZ * cellWidth;

        return x == pointX && z == pointZ;
    }
}
