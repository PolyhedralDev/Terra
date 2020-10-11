package com.dfsek.terra;

import com.dfsek.terra.command.TerraCommand;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.base.WorldConfig;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.generation.TerraChunkGenerator;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.polydev.gaea.GaeaPlugin;
import org.polydev.gaea.generation.GaeaChunkGenerator;
import org.polydev.gaea.lang.Language;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Terra extends GaeaPlugin {
    private static Terra instance;
    private static final Set<String> loadedWorlds = new HashSet<>();

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
        TerraCommand command = new TerraCommand(this);
        c.setExecutor(command);
        c.setTabCompleter(command);

        saveDefaultConfig();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, TerraChunkGenerator::saveAll, ConfigUtil.dataSave, ConfigUtil.dataSave);

    }

    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        if(!loadedWorlds.contains(worldName)) TerraWorld.loadWorld(new WorldConfig(worldName, this));
        loadedWorlds.add(worldName); // Ensure world config is only loaded once for world.
        return new TerraChunkGenerator();
    }

    public static void invalidate() {
        loadedWorlds.clear();
    }

    @Override
    public boolean isDebug() {
        return ConfigUtil.debug;
    }

    @Override
    public Class<? extends GaeaChunkGenerator> getGeneratorClass() {
        return TerraChunkGenerator.class;
    }

    @Override
    public Language getLanguage() {
        return LangUtil.getLanguage();
    }
}
