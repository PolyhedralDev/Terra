package com.dfsek.terra.generation.config;

import org.polydev.gaea.math.FastNoiseLite;

public class NoiseBuilder {
    private FastNoiseLite.NoiseType type = FastNoiseLite.NoiseType.OpenSimplex2;
    private int octaves = 1;
    private FastNoiseLite.FractalType fractalType = FastNoiseLite.FractalType.None;
    private double frequency = 0.02D;
    private double fractalGain = 0.5D;
    private double fractalLacunarity = 2.0D;
    private double pingPong = 2.0D;
    private double weightedStrength = 0.0D;
    private int seedOffset = 0;

    private FastNoiseLite.CellularDistanceFunction cellularDistanceFunction = FastNoiseLite.CellularDistanceFunction.EuclideanSq;
    private FastNoiseLite.CellularReturnType cellularReturnType = FastNoiseLite.CellularReturnType.Distance;
    private double cellularJitter = 1.0D;

    private FastNoiseLite.DomainWarpType domainWarpType = FastNoiseLite.DomainWarpType.OpenSimplex2;
    private double domainWarpAmp = 1.0D;

    private FastNoiseLite.RotationType3D rotationType3D = FastNoiseLite.RotationType3D.None;

    public FastNoiseLite build(int seed) {
        FastNoiseLite noise = new FastNoiseLite(seed + seedOffset);
        if(!fractalType.equals(FastNoiseLite.FractalType.None)) {
            noise.setFractalType(fractalType);
            noise.setFractalOctaves(octaves);
            noise.setFractalGain(fractalGain);
            noise.setFractalLacunarity(fractalLacunarity);
            if(fractalType.equals(FastNoiseLite.FractalType.PingPong)) noise.setFractalPingPongStrength(pingPong);
            noise.setFractalWeightedStrength(weightedStrength);
        }
        if(type.equals(FastNoiseLite.NoiseType.Cellular)) {
            noise.setCellularDistanceFunction(cellularDistanceFunction);
            noise.setCellularReturnType(cellularReturnType);
            noise.setCellularJitter(cellularJitter);
        }

        noise.setNoiseType(type);

        noise.setDomainWarpType(domainWarpType);
        noise.setDomainWarpAmp(domainWarpAmp);

        noise.setRotationType3D(rotationType3D);

        noise.setFrequency(frequency);
        return noise;
    }

    public NoiseBuilder setFrequency(double frequency) {
        this.frequency = frequency;
        return this;
    }

    public NoiseBuilder setFractalGain(double fractalGain) {
        this.fractalGain = fractalGain;
        return this;
    }

    public NoiseBuilder setFractalLacunarity(double fractalLacunarity) {
        this.fractalLacunarity = fractalLacunarity;
        return this;
    }

    public NoiseBuilder setFractalType(FastNoiseLite.FractalType fractalType) {
        this.fractalType = fractalType;
        return this;
    }

    public NoiseBuilder setOctaves(int octaves) {
        this.octaves = octaves;
        return this;
    }

    public NoiseBuilder setPingPong(double pingPong) {
        this.pingPong = pingPong;
        return this;
    }

    public NoiseBuilder setWeightedStrength(double weightedStrength) {
        this.weightedStrength = weightedStrength;
        return this;
    }

    public FastNoiseLite.NoiseType getType() {
        return type;
    }

    public NoiseBuilder setType(FastNoiseLite.NoiseType type) {
        this.type = type;
        return this;
    }

    public NoiseBuilder setCellularDistanceFunction(FastNoiseLite.CellularDistanceFunction cellularDistanceFunction) {
        this.cellularDistanceFunction = cellularDistanceFunction;
        return this;
    }

    public NoiseBuilder setCellularReturnType(FastNoiseLite.CellularReturnType cellularReturnType) {
        this.cellularReturnType = cellularReturnType;
        return this;
    }

    public NoiseBuilder setCellularJitter(double cellularJitter) {
        this.cellularJitter = cellularJitter;
        return this;
    }

    public int getSeedOffset() {
        return seedOffset;
    }

    public void setSeedOffset(int seedOffset) {
        this.seedOffset = seedOffset;
    }

    public NoiseBuilder setDomainWarpAmp(double domainWarpAmp) {
        this.domainWarpAmp = domainWarpAmp;
        return this;
    }

    public NoiseBuilder setDomainWarpType(FastNoiseLite.DomainWarpType domainWarpType) {
        this.domainWarpType = domainWarpType;
        return this;
    }

    public NoiseBuilder setRotationType3D(FastNoiseLite.RotationType3D rotationType3D) {
        this.rotationType3D = rotationType3D;
        return this;
    }

    public FastNoiseLite.CellularDistanceFunction getCellularDistanceFunction() {
        return cellularDistanceFunction;
    }

    public FastNoiseLite.CellularReturnType getCellularReturnType() {
        return cellularReturnType;
    }

    public FastNoiseLite.DomainWarpType getDomainWarpType() {
        return domainWarpType;
    }

    public double getCellularJitter() {
        return cellularJitter;
    }

    public double getDomainWarpAmp() {
        return domainWarpAmp;
    }

    public double getFractalGain() {
        return fractalGain;
    }

    public double getFractalLacunarity() {
        return fractalLacunarity;
    }

    public double getFrequency() {
        return frequency;
    }

    public double getPingPong() {
        return pingPong;
    }

    public double getWeightedStrength() {
        return weightedStrength;
    }

    public int getOctaves() {
        return octaves;
    }

    public FastNoiseLite.FractalType getFractalType() {
        return fractalType;
    }

    public FastNoiseLite.RotationType3D getRotationType3D() {
        return rotationType3D;
    }
}

