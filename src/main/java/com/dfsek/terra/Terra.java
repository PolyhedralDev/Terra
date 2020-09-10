package com.dfsek.terra;

import com.dfsek.terra.config.ConfigUtil;
import com.dfsek.terra.config.WorldConfig;
import com.dfsek.terra.math.NoiseFunction2;
import com.dfsek.terra.math.NoiseFunction3;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.polydev.gaea.math.parsii.eval.Expression;
import org.polydev.gaea.math.parsii.eval.Function;
import org.polydev.gaea.math.parsii.eval.Parser;

import java.util.List;

public class Terra extends JavaPlugin {

    static {
        Parser.registerFunction("floor", new Function() {
            @Override
            public int getNumberOfArguments() {
                return 1;
            }

            @Override
            public double eval(List<Expression> list) {
                return Math.floor(list.get(0).evaluate());
            }

            @Override
            public boolean isNaturalFunction() {
                return true;
            }
        });
        Parser.registerFunction("ceil", new Function() {
            @Override
            public int getNumberOfArguments() {
                return 1;
            }

            @Override
            public double eval(List<Expression> list) {
                return Math.ceil(list.get(0).evaluate());
            }

            @Override
            public boolean isNaturalFunction() {
                return true;
            }
        });
        Parser.registerFunction("round", new Function() {
            @Override
            public int getNumberOfArguments() {
                return 1;
            }

            @Override
            public double eval(List<Expression> list) {
                return Math.round(list.get(0).evaluate());
            }

            @Override
            public boolean isNaturalFunction() {
                return true;
            }
        });
        Parser.registerFunction("noise2", new NoiseFunction2());
        Parser.registerFunction("noise3", new NoiseFunction3());
    }

    private static FileConfiguration config;
    private static Terra instance;

    public static Terra getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        ConfigUtil.loadConfig(this);
        getCommand("terra").setExecutor(new TerraCommand());
        saveDefaultConfig();
        config = getConfig();
        instance = this;
    }

    @NotNull
    public static FileConfiguration getConfigFile() {
        return config;
    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        new WorldConfig(worldName, this);
        return new TerraChunkGenerator();
    }
}
