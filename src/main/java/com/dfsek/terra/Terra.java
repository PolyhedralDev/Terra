package com.dfsek.terra;

import com.dfsek.terra.command.TerraCommand;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.generation.TerraChunkGenerator;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Terra extends JavaPlugin {
    private static Terra instance;

    public static Terra getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        TerraChunkGenerator.saveAll();
    }

    @Override
    public void onEnable() {
        instance = this;
        Metrics metrics = new Metrics(this, 9017);
        metrics.addCustomChart(new Metrics.SingleLineChart("worlds", TerraWorld::numWorlds));
        Debug.setMain(this);
        ConfigUtil.loadConfig(this);

        PluginCommand c = Objects.requireNonNull(getCommand("terra"));
        TerraCommand command = new TerraCommand();
        c.setExecutor(command);
        c.setTabCompleter(command);

        saveDefaultConfig();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, TerraChunkGenerator::saveAll, ConfigUtil.dataSave, ConfigUtil.dataSave);

    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        return new TerraChunkGenerator();
    }
}
