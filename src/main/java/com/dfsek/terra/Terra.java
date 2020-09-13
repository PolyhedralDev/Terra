package com.dfsek.terra;

import com.dfsek.terra.config.ConfigUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Terra extends JavaPlugin {
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
        return new TerraChunkGenerator();
    }
}
