package com.dfsek.terra.bukkit;

import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;

import com.dfsek.terra.api.entity.CommandSender;
import com.dfsek.terra.api.event.events.platform.CommandRegistrationEvent;
import com.dfsek.terra.bukkit.world.BukkitAdapter;

import io.papermc.lib.PaperLib;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.dfsek.terra.api.command.CommandManager;
import com.dfsek.terra.api.command.exception.MalformedCommandException;
import com.dfsek.terra.api.config.ConfigPack;
import com.dfsek.terra.api.event.events.platform.PlatformInitializationEvent;
import com.dfsek.terra.bukkit.command.BukkitCommandAdapter;
import com.dfsek.terra.bukkit.command.FixChunkCommand;
import com.dfsek.terra.bukkit.command.SaveDataCommand;
import com.dfsek.terra.bukkit.generator.BukkitChunkGeneratorWrapper;
import com.dfsek.terra.bukkit.listeners.CommonListener;
import com.dfsek.terra.bukkit.listeners.PaperListener;
import com.dfsek.terra.bukkit.listeners.SpigotListener;
import com.dfsek.terra.bukkit.util.PaperUtil;
import com.dfsek.terra.commands.CommandUtil;
import com.dfsek.terra.commands.TerraCommandManager;


public class TerraBukkitPlugin extends JavaPlugin {
    public static final BukkitVersion BUKKIT_VERSION;
    
    static {
        String ver = Bukkit.getServer().getClass().getPackage().getName();
        if(ver.contains("1_17")) BUKKIT_VERSION = BukkitVersion.V1_17;
        else if(ver.contains("1_16")) BUKKIT_VERSION = BukkitVersion.V1_16;
        else if(ver.contains("1_15")) BUKKIT_VERSION = BukkitVersion.V1_15;
        else if(ver.contains("1_14")) BUKKIT_VERSION = BukkitVersion.V1_14;
        else if(ver.contains("1_13")) BUKKIT_VERSION = BukkitVersion.V1_13;
        else BUKKIT_VERSION = BukkitVersion.UNKNOWN;
    }
    
    private final TerraPluginImpl terraPlugin = new TerraPluginImpl(this);
    
    private final cloud.commandframework.CommandManager<CommandSender> commandManager;
    
    private final Map<String, com.dfsek.terra.api.world.generator.ChunkGenerator> generatorMap = new HashMap<>();
    private final Map<String, ConfigPack> worlds = new HashMap<>();
    
    public TerraBukkitPlugin() {
        try {
            commandManager = new BukkitCommandManager<>(
                    this,
                    CommandExecutionCoordinator.simpleCoordinator(),
                    BukkitAdapter::adapt,
                    BukkitAdapter::adapt
            );
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void onDisable() {
        BukkitChunkGeneratorWrapper.saveAll();
    }
    
    @Override
    public void onEnable() {
        getLogger().info("Running on version " + BUKKIT_VERSION);
        if(BUKKIT_VERSION == BukkitVersion.UNKNOWN) {
            getLogger().warning("Terra is running on an unknown Bukkit version. Proceed with caution.");
        }
        
        terraPlugin.getEventManager().callEvent(new PlatformInitializationEvent());
        
        new Metrics(this, 9017); // Set up bStats.
        
        terraPlugin.getEventManager().callEvent(new CommandRegistrationEvent(commandManager)); // Register commands
        
        
        long save = terraPlugin.getTerraConfig().getDataSaveInterval();
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, BukkitChunkGeneratorWrapper::saveAll, save,
                                                         save); // Schedule population data saving
        Bukkit.getPluginManager().registerEvents(new CommonListener(terraPlugin), this); // Register master event listener
        PaperUtil.checkPaper(this);
        
        if(PaperLib.isPaper()) {
            try {
                Class.forName("io.papermc.paper.event.world.StructureLocateEvent"); // Check if user is on Paper version with event.
                Bukkit.getPluginManager().registerEvents(new PaperListener(terraPlugin), this); // Register Paper events.
            } catch(ClassNotFoundException e) {
                registerSpigotEvents(true); // Outdated Paper version.
            }
        } else {
            registerSpigotEvents(false);
        }
    }
    
    @Override
    public @Nullable ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        return new BukkitChunkGeneratorWrapper(generatorMap.computeIfAbsent(worldName, name -> {
            if(!terraPlugin.getConfigRegistry().contains(id)) throw new IllegalArgumentException("No such config pack \"" + id + "\"");
            ConfigPack pack = terraPlugin.getConfigRegistry().get(id);
            worlds.put(worldName, pack);
            return pack.getGeneratorProvider().newInstance(pack);
        }));
    }
    
    private void registerSpigotEvents(boolean outdated) {
        if(outdated) {
            getLogger().severe("You are using an outdated version of Paper.");
            getLogger().severe("This version does not contain StructureLocateEvent.");
            getLogger().severe("Terra will now fall back to Spigot events.");
            getLogger().severe("This will prevent cartographer villagers from spawning,");
            getLogger().severe("and cause structure location to not function.");
            getLogger().severe("If you want these functionalities, update to the latest build of Paper.");
            getLogger().severe("If you use a fork, update to the latest version, then if you still");
            getLogger().severe("receive this message, ask the fork developer to update upstream.");
        } else {
            getLogger().severe("Paper is not in use. Falling back to Spigot events.");
            getLogger().severe("This will prevent cartographer villagers from spawning,");
            getLogger().severe("and cause structure location to not function.");
            getLogger().severe("If you want these functionalities (and all the other");
            getLogger().severe("benefits that Paper offers), upgrade your server to Paper.");
            getLogger().severe("Find out more at https://papermc.io/");
        }
        
        Bukkit.getPluginManager().registerEvents(new SpigotListener(terraPlugin), this); // Register Spigot event listener
    }
    
    public enum BukkitVersion {
        V1_13(13),
        
        V1_14(14),
        
        V1_15(15),
        
        V1_16(16),
        
        V1_17(17),
        
        UNKNOWN(Integer.MAX_VALUE); // Assume unknown version is latest.
        
        private final int index;
        
        BukkitVersion(int index) {
            this.index = index;
        }
        
        /**
         * Gets if this version is above or equal to another.
         *
         * @param other Other version
         *
         * @return Whether this version is equal to or later than other.
         */
        public boolean above(BukkitVersion other) {
            return this.index >= other.index;
        }
    }
    
}
