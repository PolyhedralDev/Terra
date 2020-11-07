package com.dfsek.terra;

import com.dfsek.terra.command.TerraCommand;
import com.dfsek.terra.command.structure.LocateCommand;
import com.dfsek.terra.config.base.ConfigUtil;
import com.dfsek.terra.config.base.WorldConfig;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.generation.TerraChunkGenerator;
import com.dfsek.terra.util.PaperUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.polydev.gaea.GaeaPlugin;
import org.polydev.gaea.generation.GaeaChunkGenerator;
import org.polydev.gaea.lang.Language;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Terra extends GaeaPlugin {
    private static Terra instance;
    private final Map<String, TerraChunkGenerator> generatorMap = new HashMap<>();

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

        LocateCommand locate = new LocateCommand(command, false);
        PluginCommand locatePl = Objects.requireNonNull(getCommand("locate"));
        locatePl.setExecutor(locate);
        locatePl.setTabCompleter(locate);

        saveDefaultConfig();
        //noinspection deprecation
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, TerraChunkGenerator::saveAll, ConfigUtil.dataSave, ConfigUtil.dataSave);
        Bukkit.getPluginManager().registerEvents(new EventListener(this), this);
        PaperUtil.checkPaper(this);
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
