package com.dfsek.terra.config.genconfig.noise;

import com.dfsek.terra.config.exception.ConfigException;
import com.dfsek.terra.generation.config.NoiseBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.polydev.gaea.math.FastNoiseLite;

public class NoiseConfig {
    private final NoiseBuilder builder;
    private final int dimensions;

    public NoiseConfig(ConfigurationSection section) throws ConfigException {
        NoiseBuilder builder = new NoiseBuilder();
        try {
            builder.setType(FastNoiseLite.NoiseType.valueOf(section.getString("type", "OpenSimplex2")));
            builder.setFrequency(section.getDouble("frequency", 0.02D));

            dimensions = section.getInt("dimensions", 3);
            if(dimensions != 2 && dimensions != 3)
                throw new ConfigException("Invalid number of dimensions: " + dimensions, "Noise");

            builder.setRotationType3D(FastNoiseLite.RotationType3D.valueOf(section.getString("rotation", "None")));

            if(section.contains("fractal")) {
                builder.setFractalType(FastNoiseLite.FractalType.valueOf(section.getString("fractal.type", "FBm")));
                builder.setOctaves(section.getInt("fractal.octaves", 1));
                builder.setFractalGain(section.getDouble("fractal.gain", 0.5D));
                builder.setFractalLacunarity(section.getDouble("fractal.lacunarity", 2.0D));
                builder.setPingPong(section.getDouble("fractal.ping-pong", 2.0D));
                builder.setWeightedStrength(section.getDouble("fractal.weighted-strength", 0.0D));
            }

            if(section.contains("cellular")) {
                builder.setCellularDistanceFunction(FastNoiseLite.CellularDistanceFunction.valueOf(section.getString("cellular.distance", "EuclideanSq")));
                builder.setCellularReturnType(FastNoiseLite.CellularReturnType.valueOf(section.getString("cellular.return", "Distance")));
            }

            if(section.contains("warp")) {
                builder.setDomainWarpType(FastNoiseLite.DomainWarpType.valueOf(section.getString("warp.type", "OpenSimplex2")));
                builder.setDomainWarpAmp(section.getDouble("warp.amplitude", 1.0D));
            }
            this.builder = builder;
        } catch(IllegalArgumentException | ClassCastException e) {
            throw new ConfigException(e.getMessage(), "Noise");
        }
    }

    public NoiseBuilder getBuilder() {
        return builder;
    }

    public int getDimensions() {
        return dimensions;
    }
}
