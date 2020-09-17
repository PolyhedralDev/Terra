package com.dfsek.terra.config;

import com.dfsek.terra.MaxMin;
import com.dfsek.terra.UserDefinedCarver;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.polydev.gaea.math.ProbabilityCollection;
import org.polydev.gaea.world.BlockPalette;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class CarverConfig extends YamlConfiguration {
    private UserDefinedCarver carver;
    private BlockPalette inner;
    private BlockPalette walls;
    private double[] start;
    private double[] mutate;
    private double radMutate;
    private MaxMin length;
    private MaxMin radius;
    private MaxMin height;
    private String id;
    public CarverConfig(File file) throws IOException, InvalidConfigurationException {
        super();
        this.load(file);
    }


    public MaxMin getHeight() {
        return height;
    }

    public String getID() {
        return id;
    }

    public MaxMin getLength() {
        return length;
    }

    public UserDefinedCarver getCarver() {
        return carver;
    }

    @Override
    public void load(@NotNull File file) throws IOException, InvalidConfigurationException {
        super.load(file);
        if(Objects.requireNonNull(getString("palette.interior")).startsWith("BLOCK:")) {
            inner = new BlockPalette().addBlockData(new ProbabilityCollection<BlockData>().add(Bukkit.createBlockData(getString("palette.interior").substring(6)), 1), 1);
        } else {
            inner = ConfigUtil.getPalette(getString("palette.interior")).getPalette();
        }

        if(Objects.requireNonNull(getString("palette.walls")).startsWith("BLOCK:")) {
            walls = new BlockPalette().addBlockData(new ProbabilityCollection<BlockData>().add(Bukkit.createBlockData(getString("palette.walls").substring(6)), 1), 1);
        } else {
            walls = ConfigUtil.getPalette(getString("palette.walls")).getPalette();
        }


        start = new double[] {getDouble("start.x"), getDouble("start.y"), getDouble("start.y")};
        mutate = new double[] {getDouble("mutate.x"), getDouble("mutate.y"), getDouble("mutate.z")};
        length = new MaxMin(getInt("length.min"), getInt("length.max"));
        radius = new MaxMin(getInt("start.radius.min"), getInt("start.radius.max"));
        height = new MaxMin(getInt("start.height.min"), getInt("start.height.max"));
        radMutate = getDouble("mutate.radius");
        id = getString("id");
        carver = new UserDefinedCarver(this);
    }
}
