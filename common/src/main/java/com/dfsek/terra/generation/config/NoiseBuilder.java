package com.dfsek.terra.generation.config;

import com.dfsek.tectonic.annotations.Default;
import com.dfsek.tectonic.annotations.Value;
import com.dfsek.tectonic.config.ConfigTemplate;
import com.dfsek.terra.api.math.noise.FastNoiseLite;

public class NoiseBuilder implements ConfigTemplate {
    @Value("type")
    @Default
    private FastNoiseLite.NoiseType type = FastNoiseLite.NoiseType.OpenSimplex2;

    @Value("fractal.octaves")
    @Default
    private int octaves = 1;

    @Value("fractal.type")
    @Default
    private FastNoiseLite.FractalType fractalType = FastNoiseLite.FractalType.None;

    @Value("frequency")
    @Default
    private double frequency = 0.02D;

    @Value("fractal.gain")
    @Default
    private double fractalGain = 0.5D;

    @Value("fractal.lacunarity")
    @Default
    private double fractalLacunarity = 2.0D;

    @Value("fractal.ping-pong")
    @Default
    private double pingPong = 2.0D;

    @Value("fractal.weighted-strength")
    @Default
    private double weightedStrength = 0.0D;

    @Value("offset")
    @Default
    private int seedOffset = 0;

    @Value("cellular.distance")
    @Default
    private FastNoiseLite.CellularDistanceFunction cellularDistanceFunction = FastNoiseLite.CellularDistanceFunction.EuclideanSq;

    @Value("cellular.return")
    @Default
    private FastNoiseLite.CellularReturnType cellularReturnType = FastNoiseLite.CellularReturnType.Distance;

    @Value("cellular.jitter")
    @Default
    private double cellularJitter = 1.0D;

    @Value("domain-warp.type")
    @Default
    private FastNoiseLite.DomainWarpType domainWarpType = FastNoiseLite.DomainWarpType.OpenSimplex2;

    @Value("domain-warp.amplitude")
    @Default
    private double domainWarpAmp = 1.0D;

    @Value("rotation-type")
    @Default
    private FastNoiseLite.RotationType3D rotationType3D = FastNoiseLite.RotationType3D.None;

    @Value("dimensions")
    @Default
    private int dimensions = 2;

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

    public FastNoiseLite.NoiseType getType() {
        return type;
    }

    public NoiseBuilder setType(FastNoiseLite.NoiseType type) {
        this.type = type;
        return this;
    }

    public int getSeedOffset() {
        return seedOffset;
    }

    public void setSeedOffset(int seedOffset) {
        this.seedOffset = seedOffset;
    }

    public FastNoiseLite.CellularDistanceFunction getCellularDistanceFunction() {
        return cellularDistanceFunction;
    }

    public NoiseBuilder setCellularDistanceFunction(FastNoiseLite.CellularDistanceFunction cellularDistanceFunction) {
        this.cellularDistanceFunction = cellularDistanceFunction;
        return this;
    }

    public FastNoiseLite.CellularReturnType getCellularReturnType() {
        return cellularReturnType;
    }

    public NoiseBuilder setCellularReturnType(FastNoiseLite.CellularReturnType cellularReturnType) {
        this.cellularReturnType = cellularReturnType;
        return this;
    }

    public FastNoiseLite.DomainWarpType getDomainWarpType() {
        return domainWarpType;
    }

    public NoiseBuilder setDomainWarpType(FastNoiseLite.DomainWarpType domainWarpType) {
        this.domainWarpType = domainWarpType;
        return this;
    }

    public double getCellularJitter() {
        return cellularJitter;
    }

    public NoiseBuilder setCellularJitter(double cellularJitter) {
        this.cellularJitter = cellularJitter;
        return this;
    }

    public double getDomainWarpAmp() {
        return domainWarpAmp;
    }

    public NoiseBuilder setDomainWarpAmp(double domainWarpAmp) {
        this.domainWarpAmp = domainWarpAmp;
        return this;
    }

    public double getFractalGain() {
        return fractalGain;
    }

    public NoiseBuilder setFractalGain(double fractalGain) {
        this.fractalGain = fractalGain;
        return this;
    }

    public double getFractalLacunarity() {
        return fractalLacunarity;
    }

    public NoiseBuilder setFractalLacunarity(double fractalLacunarity) {
        this.fractalLacunarity = fractalLacunarity;
        return this;
    }

    public double getFrequency() {
        return frequency;
    }

    public NoiseBuilder setFrequency(double frequency) {
        this.frequency = frequency;
        return this;
    }

    public double getPingPong() {
        return pingPong;
    }

    public NoiseBuilder setPingPong(double pingPong) {
        this.pingPong = pingPong;
        return this;
    }

    public double getWeightedStrength() {
        return weightedStrength;
    }

    public NoiseBuilder setWeightedStrength(double weightedStrength) {
        this.weightedStrength = weightedStrength;
        return this;
    }

    public int getOctaves() {
        return octaves;
    }

    public NoiseBuilder setOctaves(int octaves) {
        this.octaves = octaves;
        return this;
    }

    public FastNoiseLite.FractalType getFractalType() {
        return fractalType;
    }

    public NoiseBuilder setFractalType(FastNoiseLite.FractalType fractalType) {
        this.fractalType = fractalType;
        return this;
    }

    public FastNoiseLite.RotationType3D getRotationType3D() {
        return rotationType3D;
    }

    public NoiseBuilder setRotationType3D(FastNoiseLite.RotationType3D rotationType3D) {
        this.rotationType3D = rotationType3D;
        return this;
    }

    public int getDimensions() {
        return dimensions;
    }

    public void setDimensions(int dimensions) {
        this.dimensions = dimensions;
    }
}

