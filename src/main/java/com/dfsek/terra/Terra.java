package com.dfsek.terra;

import com.dfsek.terra.command.TerraCommand;
import com.dfsek.terra.command.structure.LocateCommand;
import com.dfsek.terra.config.base.PluginConfig;
import com.dfsek.terra.config.base.WorldConfig;
import com.dfsek.terra.config.lang.LangUtil;
import com.dfsek.terra.debug.Debug;
import com.dfsek.terra.generation.TerraChunkGenerator;
import com.dfsek.terra.listeners.EventListener;
import com.dfsek.terra.listeners.SpigotListener;
import com.dfsek.terra.registry.ConfigRegistry;
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

    @SuppressWarnings("deprecation")
    @Override
    public void onEnable() {
        Debug.setMain(this); // Set debug logger.
        instance = this; // Singleton B)

        saveDefaultConfig();

        Metrics metrics = new Metrics(this, 9017); // Set up bStats.
        metrics.addCustomChart(new Metrics.SingleLineChart("worlds", TerraWorld::numWorlds)); // World number chart.

        PluginConfig.load(this); // Load master config.yml
        LangUtil.load(PluginConfig.getLanguage(), this); // Load language.

        TerraWorld.invalidate(); // Clear/set up world cache.
        ConfigRegistry.loadAll(this); // Load all config packs.

        PluginCommand c = Objects.requireNonNull(getCommand("terra"));
        TerraCommand command = new TerraCommand(this); // Set up main Terra command.
        c.setExecutor(command);
        c.setTabCompleter(command);

        LocateCommand locate = new LocateCommand(command, false);
        PluginCommand locatePl = Objects.requireNonNull(getCommand("locate"));
        locatePl.setExecutor(locate); // Override locate command. Once Paper accepts StructureLocateEvent this will be unneeded on Paper implementations.
        locatePl.setTabCompleter(locate);


        long save = PluginConfig.getDataSaveInterval();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, TerraChunkGenerator::saveAll, save, save); // Schedule population data saving

        Bukkit.getPluginManager().registerEvents(new EventListener(this), this); // Register master event listener
        Bukkit.getPluginManager().registerEvents(new SpigotListener(this), this); // Register Spigot event listener, once Paper accepts StructureLocateEvent PR Spigot and Paper events will be separate.

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
        return PluginConfig.isDebug();
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
