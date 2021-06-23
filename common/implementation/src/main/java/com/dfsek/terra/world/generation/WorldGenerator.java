package com.dfsek.terra.world.generation;

import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.world.biome.Generator;
import com.dfsek.terra.api.world.generator.Palette;
import com.dfsek.terra.api.world.palette.holder.PaletteHolder;
import com.dfsek.terra.api.world.palette.slant.SlantHolder;

public class WorldGenerator implements Generator {
    @SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
    private final PaletteHolder palettes;
    @SuppressWarnings({"unchecked", "rawtypes", "RedundantSuppression"})
    private final SlantHolder slantPalettes;

    private final NoiseSampler noise;
    private final NoiseSampler elevation;
    private final NoiseSampler carving;

    private final NoiseSampler biomeNoise;
    private final double elevationWeight;
    private final int blendDistance;
    private final int blendStep;
    private final double blendWeight;

    public WorldGenerator(PaletteHolder palettes, SlantHolder slantPalettes, NoiseSampler noise, NoiseSampler elevation, NoiseSampler carving, NoiseSampler biomeNoise, double elevationWeight, int blendDistance, int blendStep, double blendWeight) {
        this.palettes = palettes;
        this.slantPalettes = slantPalettes;
        this.noise = noise;
        this.elevation = elevation;
        this.carving = carving;

        this.biomeNoise = biomeNoise;
        this.elevationWeight = elevationWeight;
        this.blendDistance = blendDistance;
        this.blendStep = blendStep;
        this.blendWeight = blendWeight;
    }

    @Override
    public NoiseSampler getBaseSampler() {
        return noise;
    }

    @Override
    public NoiseSampler getElevationSampler() {
        return elevation;
    }

    @Override
    public NoiseSampler getCarver() {
        return carving;
    }

    @Override
    public int getBlendDistance() {
        return blendDistance;
    }

    @Override
    public double getWeight() {
        return blendWeight;
    }

    /**
     * Gets the BlockPalette to generate the biome with.
     *
     * @return BlockPalette - The biome's palette.
     */
    @Override
    public Palette getPalette(int y) {
        return palettes.getPalette(y);
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
}
