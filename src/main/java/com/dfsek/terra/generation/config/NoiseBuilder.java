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

    private FastNoiseLite.CellularDistanceFunction cellularDistanceFunction = FastNoiseLite.CellularDistanceFunction.EuclideanSq;
    private FastNoiseLite.CellularReturnType cellularReturnType = FastNoiseLite.CellularReturnType.Distance;
    private double cellularJitter = 1.0D;

    private FastNoiseLite.DomainWarpType domainWarpType = FastNoiseLite.DomainWarpType.OpenSimplex2;
    private double domainWarpAmp = 1.0D;

    private FastNoiseLite.RotationType3D rotationType3D = FastNoiseLite.RotationType3D.None;

    public FastNoiseLite build(int seed) {
        FastNoiseLite noise = new FastNoiseLite(seed);
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

        noise.setDomainWarpType(domainWarpType);
        noise.setDomainWarpAmp(domainWarpAmp);

        noise.setRotationType3D(rotationType3D);

        noise.setFrequency(frequency);
        return noise;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public void setFractalGain(double fractalGain) {
        this.fractalGain = fractalGain;
    }

    public void setFractalLacunarity(double fractalLacunarity) {
        this.fractalLacunarity = fractalLacunarity;
    }

    public void setFractalType(FastNoiseLite.FractalType fractalType) {
        this.fractalType = fractalType;
    }

    public void setOctaves(int octaves) {
        this.octaves = octaves;
    }

    public void setPingPong(double pingPong) {
        this.pingPong = pingPong;
    }

    public void setWeightedStrength(double weightedStrength) {
        this.weightedStrength = weightedStrength;
    }

    public FastNoiseLite.NoiseType getType() {
        return type;
    }

    public void setType(FastNoiseLite.NoiseType type) {
        this.type = type;
    }

    public void setCellularDistanceFunction(FastNoiseLite.CellularDistanceFunction cellularDistanceFunction) {
        this.cellularDistanceFunction = cellularDistanceFunction;
    }

    public void setCellularReturnType(FastNoiseLite.CellularReturnType cellularReturnType) {
        this.cellularReturnType = cellularReturnType;
    }

    public void setCellularJitter(double cellularJitter) {
        this.cellularJitter = cellularJitter;
    }

    public void setDomainWarpAmp(double domainWarpAmp) {
        this.domainWarpAmp = domainWarpAmp;
    }

    public void setDomainWarpType(FastNoiseLite.DomainWarpType domainWarpType) {
        this.domainWarpType = domainWarpType;
    }

    public void setRotationType3D(FastNoiseLite.RotationType3D rotationType3D) {
        this.rotationType3D = rotationType3D;
    }
}

