package com.dfsek.terra;

import com.dfsek.terra.command.TerraCommand;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.base.WorldConfig;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.generation.TerraChunkGenerator;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.World;
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
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Terra extends GaeaPlugin {
    private static Terra instance;
    private Map<String, TerraChunkGenerator> generatorMap = new HashMap<>();

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
        return generatorMap.computeIfAbsent(worldName, name -> {
            WorldConfig c = new WorldConfig(worldName, id, this);
            TerraWorld.loadWorld(c);
            return new TerraChunkGenerator(c.getConfig());
        });
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
