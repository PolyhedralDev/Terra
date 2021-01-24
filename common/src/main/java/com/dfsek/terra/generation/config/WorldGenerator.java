package com.dfsek.terra.generation.config;

import com.dfsek.terra.api.math.noise.samplers.NoiseSampler;
import com.dfsek.terra.api.platform.block.BlockData;
import com.dfsek.terra.api.world.biome.Generator;
import com.dfsek.terra.api.world.palette.Palette;
import com.dfsek.terra.biome.palette.PaletteHolder;

public class WorldGenerator implements Generator {
    @SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
    private final PaletteHolder palettes;
    @SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
    private final PaletteHolder slantPalettes;

    private final NoiseSampler noise;
    private final NoiseSampler elevation;

    private final boolean noise2d;
    private final double base;
    private final NoiseSampler biomeNoise;
    private final double elevationWeight;
    private final int blendDistance;
    private final int blendStep;
    private final double blendWeight;

    public WorldGenerator(PaletteHolder palettes, PaletteHolder slantPalettes, NoiseSampler noise, NoiseSampler elevation, boolean noise2d, double base, NoiseSampler biomeNoise, double elevationWeight, int blendDistance, int blendStep, double blendWeight) {
        this.palettes = palettes;
        this.slantPalettes = slantPalettes;
        this.noise = noise;
        this.elevation = elevation;

        this.noise2d = noise2d;
        this.base = base;
        this.biomeNoise = biomeNoise;
        this.elevationWeight = elevationWeight;
        this.blendDistance = blendDistance;
        this.blendStep = blendStep;
        this.blendWeight = blendWeight;
    }

    @Override
    public synchronized double getElevation(int x, int z) {
        return elevation.getNoise(x, z);
    }

    @Override
    public int getBlendDistance() {
        return blendDistance;
    }

    @Override
    public double getWeight() {
        return blendWeight;
    }

    @Override
    public synchronized double getNoise(double x, double y, double z) {
        return noise.getNoise(x, y, z);
    }

    /**
     * Gets the BlockPalette to generate the biome with.
     *
     * @return BlockPalette - The biome's palette.
     */
    @Override
    public Palette<BlockData> getPalette(int y) {
        return palettes.getPalette(y);
    }

    @Override
    public boolean is2d() {
        return noise2d;
    }

    @Override
    public double get2dBase() {
        return base;
    }

    @Override
    public NoiseSampler getBiomeNoise() {
        return biomeNoise;
    }

    @Override
    public double getElevationWeight() {
        return elevationWeight;
    }

    @Override
    public int getBlendStep() {
        return blendStep;
    }

    public Palette<BlockData> getSlantPalette(int y) {
        return slantPalettes.getPalette(y);
    }
}
